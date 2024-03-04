package de.allround.kotlinweb.api.page

import de.allround.kotlinweb.api.components.Component
import de.allround.kotlinweb.util.Settings

class Page(
    val lang: String = "en",
    val head: Component,
    var body: Component,
    var loading_states: Boolean = false,
    var path_parameters: Boolean = false,
    var preload: Boolean = false,
    var remove_me: Boolean = false,
    var response_targets: Boolean = false,
    var restored: Boolean = false,
    var sse: Boolean = false,
    var ws: Boolean = false
) : Component(type = "html") {

    init {
        head.script(src = "${Settings.STATIC_ROUTE}/hyperscript.js")
        head.script(src = "${Settings.STATIC_ROUTE}/htmx.js")
        head.script(src = "${Settings.STATIC_ROUTE}/json-enc.js")
        body.addAttribute("hx-ext", "json-enc")
        if (path_parameters) {
            head.script(src = "${Settings.STATIC_ROUTE}/path-params.js")
            body.addAttribute("hx-ext", "path-params")
        }
        if (loading_states) {
            head.script(src = "${Settings.STATIC_ROUTE}/loading-states.js")
            body.addAttribute("hx-ext", "loading-states")
        }
        if (preload) {
            head.script(src = "${Settings.STATIC_ROUTE}/preload.js")
            body.addAttribute("hx-ext", "preload")
        }
        if (remove_me) {
            head.script(src = "${Settings.STATIC_ROUTE}/remove-me.js")
            body.addAttribute("hx-ext", "remove-me")
        }
        if (response_targets) {
            head.script(src = "${Settings.STATIC_ROUTE}/response-targets.js")
            body.addAttribute("hx-ext", "response-targets")
        }
        if (restored) {
            head.script(src = "${Settings.STATIC_ROUTE}/restored.js")
            body.addAttribute("hx-ext", "restored")
        }
        if (sse) {
            head.script(src = "${Settings.STATIC_ROUTE}/sse.js")
            body.addAttribute("hx-ext", "sse")
        }
        if (ws) {
            head.script(src = "${Settings.STATIC_ROUTE}/ws.js")
            body.addAttribute("hx-ext", "ws")
        }
    }
    override fun toString(): String {
        attributes["lang"] = lang
        head.init?.let { it() }
        body.init?.let { it() }

        children.add(head)
        children.add(body)

        val stylesheet = buildStylesheet()

        head.children.add(Component(type = "style", content = stylesheet))
        return super.toString()
    }
}

