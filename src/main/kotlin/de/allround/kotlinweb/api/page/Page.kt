package de.allround.kotlinweb.api.page

import de.allround.kotlinweb.api.components.Component
import de.allround.kotlinweb.api.styles.GeneratedStylesheet
import de.allround.kotlinweb.util.Settings
import io.vertx.ext.web.RoutingContext
import java.net.URI

open class Page(
    val lang: String = "en",
    val head: Component,
    var body: Component
) : Component(type = "html") {

    init {
        children.add(head)
        children.add(body)

        head.script(src = "${Settings.STATIC_ROUTE}/hyperscript.js")
        head.script(src = "${Settings.STATIC_ROUTE}/htmx.js")
        head.script(src = "${Settings.STATIC_ROUTE}/json-enc.js")
        head.meta(name = "viewport", content = "width=device-width, initial-scale=1")
        body.addAttribute("hx-ext", "json-enc")
    }



    override fun toString(): String {
        return render(null)
    }

    fun render(routingContext: RoutingContext?): String {
        attributes["lang"] = lang

        val stylesheet = buildStylesheet()

        if (routingContext == null) {
            head.addChild(Component(type = "style", id = "generated-styles", innerHTML = stylesheet.toString()))
        } else {
            val session = routingContext.session()
            head.addChild(GeneratedStylesheet.set(session, stylesheet).asComponent(session))
        }
        return "<!DOCTYPE html><html lang=\"$lang\">$head$body</html>"
    }
}

