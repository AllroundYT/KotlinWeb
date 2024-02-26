package de.allround.kotlinweb.de.allround.kotlinweb

import de.allround.kotlinweb.api.annotations.POST
import io.vertx.core.Handler
import io.vertx.core.Timer
import io.vertx.core.Vertx
import io.vertx.core.buffer.Buffer
import io.vertx.core.http.HttpMethod
import io.vertx.core.http.HttpServer
import io.vertx.core.http.HttpServerRequest
import io.vertx.core.http.HttpServerResponse
import io.vertx.core.json.JsonObject
import io.vertx.ext.auth.User
import io.vertx.ext.web.*
import io.vertx.ext.web.sstore.LocalSessionStore
import io.vertx.ext.web.sstore.SessionStore
import de.allround.kotlinweb.api.DebugLogger
import de.allround.kotlinweb.api.annotations.*
import de.allround.kotlinweb.api.components.Component
import de.allround.kotlinweb.api.components.Page
import de.allround.kotlinweb.util.HTMX
import de.allround.kotlinweb.util.MultiMap
import de.allround.kotlinweb.util.ResourceLoader
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.ext.web.handler.SessionHandler
import java.lang.reflect.Method
import java.nio.file.Path
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.function.BiFunction
import kotlin.io.path.*


class WebApplication(
    val vertx: Vertx = Vertx.vertx(),
    debugMode: Boolean = false,
    val endpoints: MutableList<Any> = ArrayList(),
    val loginRoute: String? = null,
    val sessionStore: SessionStore = LocalSessionStore.create(vertx),
    val authProvider: BiFunction<RoutingContext, Array<String>, Boolean> = BiFunction { _, permissions -> permissions.isEmpty() }
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
        if (response.headWritten()) return@Handler
        if (response.ended()) {
            context.next()
            return@Handler
        }

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

        if (response.headWritten()) return@Handler
        if (response.ended()) {
            context.next()
            return@Handler
        }


        when (result) {
            (result == null) -> {}
            is JsonObject -> context.end(result.toBuffer())
            is Buffer -> context.end(result)
            is Page -> context.end(result.init(context).render(context.session(), this))
            is Component -> context.end(result.render(context.session(), this, includeStyles = true))
            else -> context.end(result.toString())
        }

        if (response.headWritten()) return@Handler
        context.next()
    }

    fun registerStaticResources(method: HttpMethod, route: String, path: Path) {
        if (path.notExists()) return
        if (path.isRegularFile()) {

            router.route(method, route + "." + path.extension).handler {
                it.response().sendFile(path.absolutePathString())
            }

            DebugLogger.info(javaClass.simpleName, "Registered static resource ${route + "." + path.extension}")

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
                } else if (!authProvider.apply(it, permissions)) {
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

    private fun initHTMX() {
        if (HTMX.BASE_HTMX) ResourceLoader.copyResourcesIntoWorkingDirectory("/htmx/htmx.js")
        if (HTMX.json_enc) ResourceLoader.copyResourcesIntoWorkingDirectory("/htmx/json-enc.js")
        if (HTMX.class_tools) ResourceLoader.copyResourcesIntoWorkingDirectory("/htmx/class-tools.js")
        if (HTMX.path_parameters) ResourceLoader.copyResourcesIntoWorkingDirectory("/htmx/path-params.js")
        if (HTMX.client_side_templates) ResourceLoader.copyResourcesIntoWorkingDirectory("/htmx/client-side-templates.js")
        if (HTMX.loading_states) ResourceLoader.copyResourcesIntoWorkingDirectory("/htmx/loading-states.js")
        if (HTMX.preload) ResourceLoader.copyResourcesIntoWorkingDirectory("/htmx/preload.js")
        if (HTMX.remove_me) ResourceLoader.copyResourcesIntoWorkingDirectory("/htmx/remove-me.js")
        if (HTMX.response_targets) ResourceLoader.copyResourcesIntoWorkingDirectory("/htmx/response-targets.js")
        if (HTMX.restored) ResourceLoader.copyResourcesIntoWorkingDirectory("/htmx/restored.js")
        if (HTMX.sse) ResourceLoader.copyResourcesIntoWorkingDirectory("/htmx/sse.js")
        if (HTMX.ws) ResourceLoader.copyResourcesIntoWorkingDirectory("/htmx/ws.js")
    }

    fun start(port: Int = 80, host: String = "0.0.0.0") {
        initHTMX()
        router.route("/*").handler(BodyHandler.create())
        router.route("/*").handler(SessionHandler.create(sessionStore))
        registerStaticResources(HttpMethod.GET, "/static", Path("resources/"))

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
