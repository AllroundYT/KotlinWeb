import de.allround.kotlinweb.api.annotations.Fail
import de.allround.kotlinweb.api.annotations.GET
import de.allround.kotlinweb.api.annotations.ParentRoute
import de.allround.kotlinweb.api.components.Button
import de.allround.kotlinweb.api.components.Component
import de.allround.kotlinweb.api.components.Page
import de.allround.kotlinweb.api.components.misc.HtmxAttributes
import de.allround.kotlinweb.api.components.misc.Styling
import de.allround.kotlinweb.de.allround.kotlinweb.WebApplication
import de.allround.kotlinweb.util.MultiMap
import io.vertx.core.Vertx
import io.vertx.core.http.HttpServerRequest
import io.vertx.ext.web.Session
import io.vertx.ext.web.sstore.LocalSessionStore
import java.awt.Color


class Example {
    @GET("/api/counter")
    fun countUp(session: Session): Component {
        val count = session.get<Int>("counter") ?: 0
        session.put("counter", count + 1)
        return CounterComponent(session)
    }

    @GET("/")
    fun counterPage(session: Session): Page = CounterPage(session)

    @Fail(404)
    fun pageNotFound(request: HttpServerRequest): Page = Page(title = "404 - Page Not Found!", body = mutableListOf(
        Component(type = "h1", content = "Die gesuchte Seite wurde nicht gefunden! Code: 404"),
        Component(type = "p", content = "Gesuchte URI: ${request.absoluteURI()}"),
        Button(content = "Zur Startseite", htmxAttributes = HtmxAttributes(
            get = "/",
            boost = true,
            push = true,
            target = "body"
        ))
    ))
}

class CounterPage(session: Session): Page(title = "Example") {
    init {
        if (session.get<Int>("counter") == null) {
            session.put("counter", 0)
        }

        val header = Component(type = "h1", content = "Counter")
        val counterButton = Button(content = "Click me!", htmxAttributes = HtmxAttributes(
            get = "/api/counter",
            swap = "outerHTML",
            target = "#counter"
        ))

        add(header)
        add(counterButton)
        add(CounterComponent(session))
    }
}

class CounterComponent(session: Session): Component(type = "div", id = "counter") {
    init {
        if (session.get<Int>("counter") == null) {
            session.put("counter", 0)
        }

        val count = session.get<Int>("counter")!!
        val color = when  {
            count > 40 ->  Color.BLUE
            count > 30 ->  Color.GREEN
            count > 20 ->  Color.ORANGE
            count > 10 -> Color.RED
            else ->  Color.BLACK
        }
        val paragraph = Component(type = "p", content = "You clicked ${session.get<Int>("counter")} times!", styles = MultiMap("color" to "rgb(${color.red},${color.green},${color.blue})"))

        children.add(paragraph)
    }
}

fun main() {
    val vertx = Vertx.vertx()
    val webApplication = WebApplication(debugMode = true, vertx = vertx, sessionStore = LocalSessionStore.create(vertx))

    webApplication.endpoints.addAll(listOf(
        Example()
    ))

    webApplication.start()
}