package de.allround.kotlinweb

import de.allround.kotlinweb.api.annotations.Authentication
import de.allround.kotlinweb.api.annotations.Authorization
import de.allround.kotlinweb.api.annotations.Error
import de.allround.kotlinweb.api.annotations.Route
import de.allround.kotlinweb.api.annotations.methods.*
import de.allround.kotlinweb.util.RouteType
import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.core.Vertx
import io.vertx.core.http.HttpServerRequest
import io.vertx.core.http.HttpServerResponse
import io.vertx.core.json.JsonObject
import io.vertx.ext.auth.User
import io.vertx.ext.web.*
import java.lang.reflect.Method


class EndpointRegistry(private val webApplication: WebApplication) {

    val endpoints: MutableList<Any> = ArrayList()

    fun register(router: Router) {
        endpoints.forEach { endpoint ->
            val javaClass = endpoint.javaClass
            val declaredMethods = javaClass.declaredMethods
            val parentAnnotations = javaClass.annotations

            val parentRoute = StringBuilder()
            var parentRouteType = RouteType.PARENT
            var parentAuthorization: Array<String>? = null
            var parentAuthentication = false

            parentAnnotations.forEach {
                when (it) {
                    is Route -> {
                        parentRoute.append(it.route)
                        parentRouteType = it.type
                    }

                    is Authentication -> {
                        parentAuthentication = true
                    }

                    is Authorization -> {
                        parentAuthorization = it.permissions
                    }
                }
            }

            declaredMethods.filterNotNull().forEach methodRegistration@{ method: Method ->

                val annotations = method.annotations

                var route = "/"
                var routeType = RouteType.GET
                var authorization: Array<String>? = null
                var authentication = false
                var errorCode: Int? = null

                annotations.filterNotNull().forEach {
                    when (it) {
                        is GET -> route = it.route
                        is POST -> route = it.route
                        is PUT -> route = it.route
                        is DELETE -> route = it.route
                        is PATCH -> route = it.route
                        is Route -> route = it.route
                        is Authentication -> authentication = true
                        is Authorization -> authorization = it.permissions
                        is Error -> errorCode = it.statusCode
                    }
                }


                if (parentRouteType != RouteType.PARENT) {
                    routeType = parentRouteType
                }

                val finalRoute = if (parentRoute.isNotEmpty()) {
                    "$parentRoute$route"
                } else {
                    route
                }

                if (errorCode != null) {
                    router.errorHandler(errorCode!!, buildHandler(endpoint, method))
                    return@methodRegistration
                }

                if (parentAuthentication || authentication) {
                    router.route(routeType.httpMethod!!, finalRoute).handler {
                        if (!webApplication.authManagement.authentication(it)) {
                            if (webApplication.loginRouteSet) {
                                it.redirect(webApplication.loginRoute)
                                return@handler
                            }
                            it.fail(401)
                        } else {
                            it.next()
                        }
                    }
                }

                if (parentAuthorization != null || authorization != null) {
                    router.route(routeType.httpMethod!!, finalRoute).handler {
                        if (!webApplication.authManagement.authorization(it, authorization!!)) {
                            if (webApplication.loginRouteSet) {
                                it.redirect(webApplication.loginRoute)
                                return@handler
                            }
                            it.fail(403)
                        } else {
                            it.next()
                        }
                    }
                }


                router.route(routeType.httpMethod!!, finalRoute).handler(buildHandler(endpoint, method))
            }
        }
    }


    fun buildHandler(obj: Any, method: Method): Handler<RoutingContext> = Handler { context: RoutingContext ->
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
                io.vertx.ext.web.Route::class.java -> paramsToInvoke.add(context.currentRoute())
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
        sendResponseIfNotNull(result, context)
    }

    private fun sendResponseIfNotNull(response: Any?, context: RoutingContext) {
        when (response) {
            (response == null) -> {
                if (context.response().headWritten() || context.response().ended()) return
                context.next()
            }

            is Future<out Any> -> {
                response.onSuccess {
                    sendResponseIfNotNull(it, context)
                }.onFailure {
                    context.fail(it)
                }
            }

            else -> response?.let {
                context.end(it.toString())
            }
        }
    }
}