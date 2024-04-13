import de.allround.kotlinweb.WebApplication
import de.allround.kotlinweb.api.annotations.Authentication
import de.allround.kotlinweb.api.annotations.Error
import de.allround.kotlinweb.api.html.Component
import de.allround.kotlinweb.api.html.Page
import de.allround.kotlinweb.api.html.page
import io.vertx.ext.web.Session

fun main() {
    val app = WebApplication()
    app.registerEndpoints(object : Any() {
        @Error(404)
        @Authentication
        fun test(session: Session): Page = page {
            println(session.id())
            addChild(Component(type = "h1", innerHTML = "TEST"))
        }
    })
    app.start()
}