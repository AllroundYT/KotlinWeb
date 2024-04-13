import de.allround.kotlinweb.WebApplication
import de.allround.kotlinweb.api.action.trigger.DomEvents
import de.allround.kotlinweb.api.action.trigger.Trigger
import de.allround.kotlinweb.api.components.Component
import de.allround.kotlinweb.api.components.misc.TextType
import de.allround.kotlinweb.api.misc.DebugLogger
import de.allround.kotlinweb.api.page
import de.allround.kotlinweb.api.page.Page
import de.allround.kotlinweb.api.rest.GET
import de.allround.kotlinweb.api.fragment
import io.vertx.ext.web.Session
import java.awt.Color

fun main() {
    DebugLogger.isEnabled = true
    val webApplication = WebApplication()

    webApplication.endpoints.add(Test)

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
                            target = ".counter_value"
                        }
                    }
                }

                text(text = "You clicked ${
                    getCounterValueSpan(session)
                } times!")
            }

            style(".counter_value") {
                add("color", Color(0, 111, 180))
            }
        }
    }

    @GET("/count_up")
    fun getCountUp(session: Session): Component {

        val counter: Int = getCounterValue(session) + 1
        session.put("counter", counter)

        return getCounterValueSpan(session)
    }

    private fun getCounterValueSpan(session: Session): Component = fragment {
        val counterValue = getCounterValue(session)
        text(type = TextType.SPAN, text = counterValue.toString()) {
            renderAsChildren = false
            classes.add("counter_value")
        }

        if (counterValue > 10) {
            style(selector = ".counter_value") {
                add("text-decoration", "underline")
            }
        }
    }
}