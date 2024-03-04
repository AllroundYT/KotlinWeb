import de.allround.kotlinweb.WebApplication
import de.allround.kotlinweb.api.action.trigger.DomEvents
import de.allround.kotlinweb.api.action.trigger.Trigger
import de.allround.kotlinweb.api.components.Component
import de.allround.kotlinweb.api.page
import de.allround.kotlinweb.api.page.Page
import de.allround.kotlinweb.api.rest.GET
import de.allround.kotlinweb.api.root
import io.vertx.ext.web.Session
import java.awt.Color

fun main() {
    val webApplication = WebApplication(debugMode = true)

    webApplication.endpoints.addAll(
        listOf(Test)
    )

    webApplication.start()
}

object Test {
    private fun getCounterValue(session: Session): Int = if (session.get<Int>("counter") != null) {
        session.get("counter")
    } else {
        0
    }

    @GET("/")
    fun getHomepage(session: Session): Page {
        return page(title = "Dashboard") {
            div {
                button(text = "Increase counter!") {
                    on(Trigger.DomEvent(DomEvents.CLICK)) {
                        get("/count_up") {
                            target = "#counter_value"
                        }
                    }
                }

                text(text = "You clicked ${getCounterValue(session)} times!") {
                    id = "counter_value"
                }
            }

            style(".counter_value") {
                add("color", Color(0, 111, 180))
            }
        }
    }

    @GET("/count_up")
    fun countUp(session: Session): Component = root {
        val counter: Int = getCounterValue(session) + 1
        session.put("counter", counter)

        text(text = "You clicked $counter times!") {
            id = "counter_value"
        }
    }
}