package de.allround.kotlinweb

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

    private val endpointRegistryOLD: EndpointRegistry = EndpointRegistry(this)
    private val preRouteHandlers: MutableSet<Handler<RoutingContext>> = HashSet()
    internal val loginRouteSet: Boolean get() = loginRoute != null
    private val httpServer: HttpServer = vertx.createHttpServer()
    private val router: Router = Router.router(vertx)

    fun registerEndpoints(vararg obj: Any): WebApplication {
        endpointRegistryOLD.endpoints.addAll(obj)
        return this
    }

    fun registerPreRouteHandler(vararg handlers: Handler<RoutingContext>): WebApplication {
        preRouteHandlers.addAll(handlers)
        return this
    }


    fun start(port: Int = 80, host: String = "0.0.0.0") {
        router.route("/*").handler(BodyHandler.create())
        preRouteHandlers.forEach {
            router.route("/*").handler(it)
        }
        router.route("/*").handler(SessionHandler.create(sessionStore))

        endpointRegistryOLD.register(router)

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
