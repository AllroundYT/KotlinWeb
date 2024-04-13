import de.allround.kotlinweb.WebApplication
import de.allround.kotlinweb.api.action.htmx.server.ServerAction
import de.allround.kotlinweb.api.action.trigger.DomEvents
import de.allround.kotlinweb.api.action.trigger.Trigger
import de.allround.kotlinweb.api.misc.DebugLogger
import de.allround.kotlinweb.api.page
import de.allround.kotlinweb.api.page.Page
import de.allround.kotlinweb.api.rest.GET

fun main() {
    DebugLogger.isEnabled = true
    WebApplication(endpoints = mutableListOf(StyleTest)).start()
}

object StyleTest {

    val action = ServerAction {
        println("TEST")
    }

    @GET("/")
    fun test(): Page = page {

        on(Trigger.DomEvent(DomEvents.LOAD)) {
            serverAction(action)
        }
    }
}
