package de.allround.kotlinweb.api

import de.allround.kotlinweb.api.components.Component
import de.allround.kotlinweb.api.styles.Styling

fun root(init: Component.() -> Unit): Component {
    val component = Component(type = "div")
    component.init()
    return component
}

fun style(selector: String, init: Styling.() -> Unit): Styling {
    val styling = Styling(selector)
    styling.init()
    return styling
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