package de.allround.kotlinweb

import de.allround.kotlinweb.api.components.Component
import de.allround.kotlinweb.api.rest.POST
import io.vertx.core.Handler
import io.vertx.core.Timer
import io.vertx.core.Vertx
import io.vertx.core.http.HttpMethod
import io.vertx.core.http.HttpServer
import io.vertx.core.http.HttpServerRequest
import io.vertx.core.http.HttpServerResponse
import io.vertx.core.json.JsonObject
import io.vertx.ext.auth.User
import io.vertx.ext.web.*
import io.vertx.ext.web.sstore.LocalSessionStore
import io.vertx.ext.web.sstore.SessionStore
import de.allround.kotlinweb.api.misc.DebugLogger
import de.allround.kotlinweb.api.page.Page
import de.allround.kotlinweb.api.rest.*
import de.allround.kotlinweb.util.MultiMap
import de.allround.kotlinweb.util.ResourceLoader
import de.allround.kotlinweb.util.Settings
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.ext.web.handler.HSTSHandler
import io.vertx.ext.web.handler.SessionHandler
import java.lang.reflect.Method
import java.nio.file.Path
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.io.path.*


class WebApplication(
    val vertx: Vertx = Vertx.vertx(),
    debugMode: Boolean = false,
    val endpoints: MutableList<Any> = mutableListOf(),
    val allRouteHandlers: List<Handler<RoutingContext>> = listOf(),
    val loginRoute: String? = null,
    val sessionStore: SessionStore = LocalSessionStore.create(vertx),
    val authProvider: Function2<RoutingContext, Array<String>, Boolean> = { _, permissions -> permissions.isEmpty() },
    val bulmaCss: Boolean = false
) {


    private val styles: MultiMap<String, Pair<String, String>> = MultiMap()
    private val halfHourTimer: Timer = vertx.timer(30, TimeUnit.MINUTES)
    private val loginRouteSet: Boolean get() = loginRoute != null
    private val httpServer: HttpServer = vertx.createHttpServer()
    private val router: Router = Router.router(vertx)


    fun registerStyles(session: Session, style: String): String {
        val id = UUID.randomUUID().toString().lowercase().replace("-", "")
        styles.add(session.id(), id to style)
        return id
    }

    init {
        DebugLogger.isEnabled = debugMode

        halfHourTimer.onComplete {
            styles.map.forEach { (sessionId, _) ->
                sessionStore.get(sessionId).onComplete {
                    if (it.failed() || it.result() == null || it.result().isDestroyed) {
                        this.styles.remove(sessionId)
                    }
                }
            }
        }
    }

    private fun registerFailHandler(obj: Any, method: Method) {
        if (!method.isAnnotationPresent(Fail::class.java)) return
        router.errorHandler(method.getAnnotation(Fail::class.java).statusCode, invokeMethod(obj, method))
    }


    private fun invokeMethod(obj: Any, method: Method): Handler<RoutingContext> = Handler { context ->
        val response: HttpServerResponse = context.response()
        if (response.headWritten() || response.ended()) return@Handler

        val parameters = method.parameters
        val paramsToInvoke = ArrayList<Any?>()


        parameters.forEach {
            when (it.type) {
                RoutingContext::class.java -> paramsToInvoke.add(context)
                HttpServerRequest::class.java -> paramsToInvoke.add(context.request())
                HttpServerResponse::class.java -> paramsToInvoke.add(context.response())
                Session::class.java -> paramsToInvoke.add(context.session())
                User::class.java -> paramsToInvoke.add(context.user())
                RequestBody::class.java -> paramsToInvoke.add(context.body())
                ParsedHeaderValues::class.java -> paramsToInvoke.add(context.parsedHeaders())
                LanguageHeader::class.java -> paramsToInvoke.add(context.preferredLanguage())
                Route::class.java -> paramsToInvoke.add(context.currentRoute())
                Throwable::class.java -> paramsToInvoke.add(context.failure())
                Vertx::class.java -> paramsToInvoke.add(context.vertx())
                JsonObject::class.java -> paramsToInvoke.add(context.body().asJsonObject())
                else -> paramsToInvoke.add(Object())
            }
        }
        val result: Any? = if (method.parameterCount == 0) {
            method.invoke(obj)
        } else {
            method.invoke(obj, *paramsToInvoke.toArray())
        }

        if (response.headWritten() || response.ended()) return@Handler


        when (result) {
            (result == null) -> {}
            is Page -> {
                val rawResponse = result.toString()
                DebugLogger.info("Page", rawResponse)
                context.end(rawResponse)
            }
            is Component -> {
                val rawResponse = result.toString()
                val stylesheet = result.buildStylesheet()
                val styleComponent = Component(type = "style", content = stylesheet.toString())
                DebugLogger.info("Component", "$styleComponent\n$rawResponse")
                context.end(rawResponse)
            }
            else -> {
                val rawResponse = result.toString()
                DebugLogger.info("Response", rawResponse)
                context.end(rawResponse)
            }
        }

        if (response.headWritten() || response.ended()) return@Handler
        context.next()
    }

    private fun registerStaticResources(method: HttpMethod, route: String, path: Path) {
        if (path.notExists()) return
        if (path.isRegularFile()) {

            router.route(method, route + "." + path.extension).handler {
                it.response().sendFile(path.absolutePathString())
            }

            DebugLogger.info(javaClass.simpleName, "Registered static resource ${path.toAbsolutePath()} ${route + "." + path.extension}")

        } else {
            path.forEachDirectoryEntry {
                registerStaticResources(method, route + "/" + it.fileName.nameWithoutExtension, it)
            }
        }
    }

    fun registerGeneratedStylesheet(css: String, route: String) {
        router.get(route).handler {
            if (!it.response().ended()) it.end(css)
        }
    }

    private fun registerAuthentication(httpMethod: HttpMethod, route: String, method: Method) {
        val authorization: Authorization? =
            if (method.isAnnotationPresent(Authorization::class.java)) method.getAnnotation(Authorization::class.java) else null
        if (authorization != null) {
            val permissions = authorization.permissions
            router.route(httpMethod, route).handler {
                if (it.user() == null) {
                    if (loginRouteSet) {
                        it.redirect(loginRoute)
                        return@handler
                    }
                    it.fail(403)
                } else if (!authProvider.invoke(it, permissions)) {
                    it.fail(403)
                } else it.next()
            }
        }
    }

    private fun registerAuthorization(httpMethod: HttpMethod, route: String, method: Method) {
        val authentication = method.isAnnotationPresent(Authentication::class.java)
        if (authentication) {
            router.route(httpMethod, route).handler {
                if (it.user() == null) {
                    if (loginRouteSet) {
                        it.redirect(loginRoute)
                        return@handler
                    }
                    it.fail(403)
                } else it.next()
            }
        }
    }


    private fun extractMethodRoute(method: Method): String? {
        return when {
            method.isAnnotationPresent(GET::class.java) -> method.getAnnotation(GET::class.java).route
            method.isAnnotationPresent(POST::class.java) -> method.getAnnotation(POST::class.java).route
            method.isAnnotationPresent(PUT::class.java) -> method.getAnnotation(PUT::class.java).route
            method.isAnnotationPresent(DELETE::class.java) -> method.getAnnotation(DELETE::class.java).route
            method.isAnnotationPresent(PATCH::class.java) -> method.getAnnotation(PATCH::class.java).route
            else -> null
        }
    }

    private fun registerRequestHandler(obj: Any, method: Method) {
        val route: String? = if (obj.javaClass.isAnnotationPresent(ParentRoute::class.java)) {
            obj.javaClass.getAnnotation(ParentRoute::class.java).route + extractMethodRoute(method)
        } else {
            extractMethodRoute(method)
        }

        if (route == null) return


        val httpMethod: HttpMethod? = when {
            method.isAnnotationPresent(GET::class.java) -> HttpMethod.GET
            method.isAnnotationPresent(POST::class.java) -> HttpMethod.POST
            method.isAnnotationPresent(PUT::class.java) -> HttpMethod.PUT
            method.isAnnotationPresent(DELETE::class.java) -> HttpMethod.DELETE
            method.isAnnotationPresent(PATCH::class.java) -> HttpMethod.PATCH
            else -> null
        }

        if (httpMethod == null) return

        registerAuthentication(httpMethod, route, method)
        registerAuthorization(httpMethod, route, method)
        router.route(httpMethod, route).handler(invokeMethod(obj, method))
        DebugLogger.info(
            javaClass.simpleName, "Registered endpoint! Method: $httpMethod Route: $route Authentication: ${
                method.isAnnotationPresent(Authentication::class.java)
            } Authorization: ${
                if (method.isAnnotationPresent(Authorization::class.java)) "[${
                    method.getAnnotation(
                        Authorization::class.java
                    ).permissions.joinToString(", ")
                }]" else "null"
            }"
        )
    }


    private fun registerEndpoints(obj: Any): WebApplication {

        obj.javaClass.declaredMethods.forEach { method ->
            registerFailHandler(obj, method)
            registerRequestHandler(obj, method)
        }

        return this
    }

    private fun copyResourcesIntoStaticDir() {
        ResourceLoader.copyResources("/hyperscript.js")
        ResourceLoader.copyResources("/htmx.js")
        ResourceLoader.copyResources("/json-enc.js")
    }

    fun start(port: Int = 80, host: String = "0.0.0.0") {
        copyResourcesIntoStaticDir()

        router.route("/*").handler(BodyHandler.create())
        allRouteHandlers.forEach {
            router.route("/*").handler(it)
        }
        router.route("/*").handler(SessionHandler.create(sessionStore))
        registerStaticResources(HttpMethod.GET, Settings.STATIC_ROUTE, Settings.STATIC_DIR)

        endpoints.forEach {
            registerEndpoints(it)
        }

        httpServer.exceptionHandler {
            DebugLogger.exception(javaClass.simpleName, it, "Handling routing exception:")
        }
        httpServer.invalidRequestHandler {
            DebugLogger.exception(javaClass.simpleName, "Invalid request: ${it.absoluteURI()} - ${it.localAddress()}")
        }
        httpServer.requestHandler(router)
        httpServer.listen(port, host)
        DebugLogger.info(javaClass.simpleName, "HttpServer started!")
    }


    fun stop() {
        httpServer.close {
            DebugLogger.info(javaClass.simpleName, "HttpServer stopped!")
        }
    }
}
