package de.allround.kotlinweb.api.action.htmx.server

import io.vertx.ext.web.RoutingContext
import java.util.UUID

data class ServerAction(val include: Array<String> = arrayOf(),val handler: (RoutingContext) -> Unit){
    val uuid = UUID.randomUUID()
    val id = "${uuid.leastSignificantBits}${uuid.mostSignificantBits}"

    init {
        ServerActions.register(this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ServerAction

        if (!include.contentEquals(other.include)) return false
        if (handler != other.handler) return false
        if (uuid != other.uuid) return false
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = include.contentHashCode()
        result = 31 * result + handler.hashCode()
        result = 31 * result + (uuid?.hashCode() ?: 0)
        result = 31 * result + id.hashCode()
        return result
    }
}