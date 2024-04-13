package de.allround.kotlinweb.util

import io.vertx.core.http.HttpMethod

enum class RouteType(val httpMethod: HttpMethod?) {
    GET(HttpMethod.GET),
    POST(HttpMethod.POST),
    PUT(HttpMethod.PUT),
    DELETE(HttpMethod.DELETE),
    PATCH(HttpMethod.PATCH),
    HEAD(HttpMethod.HEAD),
    CONNECT(HttpMethod.CONNECT),
    OPTIONS(HttpMethod.OPTIONS),
    TRACE(HttpMethod.TRACE),
    PARENT(null)
}