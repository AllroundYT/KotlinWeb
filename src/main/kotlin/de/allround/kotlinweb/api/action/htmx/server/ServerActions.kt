package de.allround.kotlinweb.api.action.htmx.server

import de.allround.kotlinweb.api.annotations.methods.POST
import io.vertx.ext.web.RoutingContext

object ServerActions {

    private val actions: MutableMap<String, ServerAction> = mutableMapOf()

    @POST("/*")
    fun getServerAction(context: RoutingContext) {
        var executedAction = false
        val headers = context.request().headers()
        if (!headers.contains("KW-SERVER-ACTION")) {
            context.next()
            return
        }

        headers.getAll("KW-SERVER-ACTION").forEach { header ->
            val action: ServerAction? = actions[header]
            if (action != null) {
                executedAction = true
                action.handler.invoke(context)
            }
        }

        if (executedAction) {
            context.end()
            return
        }
        context.next()
    }

    fun register(serverAction: ServerAction) {
        actions[serverAction.id] = serverAction
    }
}