package de.allround.kotlinweb.api.html

import de.allround.kotlinweb.api.html.tags.*

open class Page(
    val lang: String = "en",
    val title: String = "Generated with KotlinWeb",
    val pageInit: Page.(head: Component<Head>, body: Component<Body>) -> Unit
) : Component<Page>(type = "html") {

    override fun init() {
        pageInit(head, body)
    }

    val head = Head()
    val body = Body()

    init {
        attributes["lang"] = lang
        children.add(head)
        children.add(body)

        head.add(Title(title))
        head.add(MetaInfo("viewport","width=device-width, initial-scale=1"))
        head.add(Script( "/static/htmx"))
        head.add(Script("/static/json-enc"))
        head.add(Script("/static/hyperscript"))
    }

    override fun <T : Component<T>> first(component: T): T = body.first(component)

    override fun <T : Component<T>> last(component: T): T = body.last(component)

    override fun <T : Component<T>> add(component: T): T = body.add(component)

    override fun toString(): String {
        return  "<!DOCTYPE html>${super.toString()}"
    }
}

