package de.allround.kotlinweb.api.page

import io.vertx.ext.web.RoutingContext

interface PageProvider {
    fun getPage(routingContext: RoutingContext): Page
}