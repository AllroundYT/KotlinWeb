package de.allround.kotlinweb

import io.vertx.ext.web.RoutingContext

interface AuthManagement {
    fun authentication(routingContext: RoutingContext): Boolean
    fun authorization(routingContext: RoutingContext, neededPermissions: Array<String>): Boolean

    object DefaultImplementation : AuthManagement {
        override fun authentication(routingContext: RoutingContext): Boolean = routingContext.user() != null

        override fun authorization(routingContext: RoutingContext, neededPermissions: Array<String>): Boolean = neededPermissions.isEmpty()
    }
}