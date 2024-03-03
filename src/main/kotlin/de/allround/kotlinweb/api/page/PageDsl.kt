package de.allround.kotlinweb.api.page

import de.allround.kotlinweb.api.components.Component

class Page(val lang: String = "en", val head: Component, var body: Component) : Component(type = "html") {
    override fun toString(): String {
        attributes["lang"] = lang
        head.init?.let { it() }
        body.init?.let { it() }

        val extensionAttribute: String? = body.attributes["hx-ext"]
        if (extensionAttribute == null) {
            body.attributes["hx-ext"] = "kotlin-web"
        } else if (!extensionAttribute.contains("kotlin-web")) {
            body.attributes["hx-ext"] += ", kotlin-web"
        }
        children.add(head)
        children.add(body)
        return super.toString()
    }
}

fun page(title: String = "Kotlin Web generated WebPage", lang: String = "en", init: Component.(Page) -> Unit): Page {
    val head = Component(type = "head")
    val body = Component(type = "body")

    head.children.add(Component(type = "title", content = title))
    head.children.add(Component(type = "script") {
        attributes["src"] = "/kw-internal/htmx/htmx.js"
    })
    head.children.add(Component(type = "script") {
        attributes["src"] = "/kw-internal/htmx/kotlin-web.js"
    })
    head.children.add(Component(type = "link") {
        attributes["rel"] = "stylesheet"
        attributes["href"] = "/kw-internal/styles"
        classes.add("kw-style")
    })

    val page = Page(lang = lang,head = head, body = body)
    body.init(page)
    return page
}