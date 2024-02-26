package de.allround.kotlinweb.api.components

import de.allround.kotlinweb.de.allround.kotlinweb.WebApplication
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.Session
import de.allround.kotlinweb.util.HTMX
import de.allround.kotlinweb.util.MultiMap

open class Page(
    var lang: String = "en",
    var title: String,
    val head: MutableList<Component> = mutableListOf(),
    val body: MutableList<Component> = mutableListOf()
) {

    open fun init(routingContext: RoutingContext): Page {
        return this
    }

    protected fun add(component: Component) {
        body.add(component)
    }

    private fun generateStylesheet(): String {

        val builder = StringBuilder()

        head.filter {
            it.type == "style" && it.content != null
        }.forEach {
            builder.append(it.content)
        }


        body.forEach {
            builder.append(it.generateStylesheet())
        }

        return builder.toString()
    }

    fun render(session: Session, webApplication: WebApplication): String {

        val stylesheetId = webApplication.registerStyles(session, generateStylesheet())
        val html = Component(type = "html", attributes = MultiMap("lang" to lang))

        val head = Head(title = title)
        head.children.add(HeadLink(rel = "stylesheet", href = "/styles/$stylesheetId"))
        val body = Body()

        html.children.add(head)
        html.children.add(body)

        //including htmx and used extensions
        if (HTMX.BASE_HTMX) {
            head.children.add(Script(src = "/static/htmx/htmx.js"))
        }
        if (HTMX.class_tools) {
            head.children.add(Script(src = "/static/htmx/class-tools.js"))
            body.attributes.add("hx-ext", "class-tools")
        }
        if (HTMX.path_parameters) {
            head.children.add(Script(src = "/static/htmx/path-params.js"))
            body.attributes.add("hx-ext", "path-params")
        }
        if (HTMX.client_side_templates) {
            head.children.add(Script(src = "/static/htmx/client-side-templates.js"))
            body.attributes.add("hx-ext", "client-side-templates")
        }
        if (HTMX.loading_states) {
            head.children.add(Script(src = "/static/htmx/loading-states.js"))
            body.attributes.add("hx-ext", "loading-states")
        }
        if (HTMX.preload) {
            head.children.add(Script(src = "/static/htmx/preload.js"))
            body.attributes.add("hx-ext", "preload")
        }
        if (HTMX.remove_me) {
            head.children.add(Script(src = "/static/htmx/remove-me.js"))
            body.attributes.add("hx-ext", "remove-me")
        }
        if (HTMX.response_targets) {
            head.children.add(Script(src = "/static/htmx/response-targets.js"))
            body.attributes.add("hx-ext", "response-targets")
        }
        if (HTMX.restored) {
            head.children.add(Script(src = "/static/htmx/restored.js"))
            body.attributes.add("hx-ext", "restored")
        }
        if (HTMX.sse) {
            head.children.add(Script(src = "/static/htmx/sse.js"))
            body.attributes.add("hx-ext", "sse")
        }
        if (HTMX.ws) {
            head.children.add(Script(src = "/static/htmx/ws.js"))
            body.attributes.add("hx-ext", "ws")
        }
        if (HTMX.json_enc) {
            head.children.add(Script(src = "/static/htmx/json-enc.js"))
            body.attributes.add("hx-ext", "json-enc")
        }

        head.children.addAll(this.head)
        body.children.addAll(this.body)

        return html.render(session, webApplication)
    }

}
