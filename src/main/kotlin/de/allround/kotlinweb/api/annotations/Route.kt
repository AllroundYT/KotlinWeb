package de.allround.kotlinweb.api.annotations

import de.allround.kotlinweb.util.RouteType

annotation class Route(val route: String, val type: RouteType = RouteType.PARENT)
