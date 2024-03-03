
import de.allround.kotlinweb.WebApplication
import de.allround.kotlinweb.api.action.misc.DomEvents
import de.allround.kotlinweb.api.action.misc.Trigger
import de.allround.kotlinweb.api.page.Page
import de.allround.kotlinweb.api.page.page
import de.allround.kotlinweb.api.rest.GET
import io.vertx.ext.web.RoutingContext

fun main() {
    val webApplication = WebApplication(debugMode = true)

    webApplication.endpoints.addAll(
        listOf(Test)
    )

    webApplication.start()
}

object Test {
    @GET("/")
    fun home(routingContext: RoutingContext): Page {
        return page (title = "Dashboard") {
            div {
                button(text = "Click me!") {
                    script(Trigger.DomEvent(DomEvents.CLICK)) {
                        addClass("test1")
                        toggleClass("test2")
                    }

                    script(Trigger.Custom("testEvent")) {
                        removeClass("test3")
                    }
                }
            }
            div {
                text(text = "Test Rot") {
                    classes.add("red")
                }
            }
        }
    }
}