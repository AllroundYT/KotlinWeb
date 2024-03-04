package de.allround.kotlinweb.api

import de.allround.kotlinweb.api.components.Component
import de.allround.kotlinweb.api.components.NotRenderedComponent
import de.allround.kotlinweb.api.page.Page

fun div(init: Component.() -> Unit): Component {
    val component = Component(type = "div")
    component.init()
    return component
}

fun root(init: Component.() -> Unit): Component {
    val component = NotRenderedComponent()
    component.init()
    return component
}


fun page(title: String = "Kotlin Web generated WebPage", lang: String = "en", init: Component.(Page) -> Unit): Page {
    val head = Component(type = "head")
    val body = Component(type = "body")
    val page = Page(lang = lang, head = head, body = body)

    head.children.add(Component(type = "title", content = title))
    body.init(page)
    return page
}

val Number.px: String
    get() = "${this}px"

val Number.em: String
    get() = "${this}em"

val Number.percent: String
    get() = "${this}%"

val Number.vh: String
    get() = "${this}vh"

val Number.vw: String
    get() = "${this}vw"

val Number.vmin: String
    get() = "${this}vmin"

val Number.vmax: String
    get() = "${this}vmax"

val Number.rem: String
    get() = "${this}rem"

val Number.ch: String
    get() = "${this}ch"

val Number.mm: String
    get() = "${this}mm"

val Number.cm: String
    get() = "${this}cm"