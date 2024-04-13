package de.allround.kotlinweb.api.html

fun html(init: Component.() -> Unit): Component {
    val component = object : Component(type = "") {
        override fun toString(): String {
            val builder = StringBuilder()
            this.children.forEach {
                if (it.renderAsChildren) {
                    builder.append(it.toString()).append("\n")
                }
            }
            return builder.toString()
        }
    }
    component.init()
    return component
}


fun page(title: String = "Kotlin Web generated WebPage", lang: String = "en", init: Component.(Page) -> Unit): Page {
    val head = Component(type = "head")
    val body = Component(type = "body")
    val page = Page(lang = lang, head = head, body = body)
    head.addChild(Component(type = "title", innerHTML = title))
    body.init(page)
    return page
}