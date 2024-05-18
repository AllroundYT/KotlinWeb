import de.allround.kotlinweb.WebApplication
import de.allround.kotlinweb.api.action.trigger.DomEvents
import de.allround.kotlinweb.api.action.trigger.Trigger
import de.allround.kotlinweb.api.annotations.methods.GET
import de.allround.kotlinweb.api.html.BaseComponent
import de.allround.kotlinweb.api.html.Component
import de.allround.kotlinweb.api.html.Page


object TestWebApp {
    @GET("/")
    fun home(): Page = Page { _, _ ->
        val component = add(BaseComponent(type = "button", innerHTML = "Klick mich!") {
            on(Trigger.DomEvent(DomEvents.CLICK)) {
                incrementVariable("\$counter")
                setInnerHTML("\$counter")
            }
        })
    }
}

fun main() {
    val app = WebApplication()
    app.registerEndpoints(TestWebApp)
    app.start()
}