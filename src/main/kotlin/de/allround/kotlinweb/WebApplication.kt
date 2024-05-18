package de.allround.kotlinweb

import de.allround.kotlinweb.api.action.htmx.server.ServerActions
import de.allround.kotlinweb.api.annotations.methods.GET
import de.allround.kotlinweb.util.ResourceLoader
import io.vertx.core.Handler
import io.vertx.core.Vertx
import io.vertx.core.http.HttpServer
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.ext.web.handler.SessionHandler
import io.vertx.ext.web.sstore.LocalSessionStore
import io.vertx.ext.web.sstore.SessionStore


class WebApplication(
    private val vertx: Vertx = Vertx.vertx(),
    val loginRoute: String? = null,
    private val sessionStore: SessionStore = LocalSessionStore.create(vertx),
    internal val authManagement: AuthManagement = AuthManagement.DefaultImplementation
) {

    private val endpointRegistry: EndpointRegistry = EndpointRegistry(this)
    private val preRouteHandlers: MutableSet<Handler<RoutingContext>> = HashSet()
    internal val loginRouteSet: Boolean get() = loginRoute != null
    private val httpServer: HttpServer = vertx.createHttpServer()
    private val router: Router = Router.router(vertx)

    fun registerEndpoints(vararg obj: Any): WebApplication {
        endpointRegistry.endpoints.addAll(obj)
        return this
    }

    fun registerPreRouteHandler(vararg handlers: Handler<RoutingContext>): WebApplication {
        preRouteHandlers.addAll(handlers)
        return this
    }
    

    fun start(port: Int = 80, host: String = "0.0.0.0") {

        ResourceLoader.copyResources("/htmx.js","/hyperscript.js","/json-enc.js")
        

        router.route("/*").handler(BodyHandler.create())
        preRouteHandlers.forEach {
            router.route("/*").handler(it)
        }
        router.route("/*").handler(SessionHandler.create(sessionStore))

        registerEndpoints(ServerActions)
        registerEndpoints(object : Any() {
            @GET("/static/htmx")
            fun getHtmxSource(context: RoutingContext) {
                context.response().sendFile("./static/htmx.js")
            }
            @GET("/static/hyperscript")
            fun getHyperscriptSource(context: RoutingContext) {
                context.response().sendFile("./static/hyperscript.js")
            } 
            @GET("/static/json-enc")
            fun getJsonEncSource(context: RoutingContext) {
                context.response().sendFile("./static/json-enc.js")
            }
        })

        endpointRegistry.register(router)

        httpServer.exceptionHandler {
            it.printStackTrace(System.err)
        }
        httpServer.invalidRequestHandler {
            System.err.println("Invalid request: ${it.absoluteURI()} - ${it.localAddress()}")
        }
        httpServer.requestHandler(router)
        httpServer.listen(port, host).onSuccess {
            println("HttpServer started!")
        }.onFailure {
            it.printStackTrace(System.err)
        }
    }


    fun stop() {
        httpServer.close().onSuccess {
            println("HttpServer stopped!")
        }.onFailure {
            it.printStackTrace(System.err)
        }
    }
}
