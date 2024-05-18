package de.allround.kotlinweb.api.html.tags

import de.allround.kotlinweb.api.html.Component

class Script(val src: String) : Component<Script>(type = "script") {
    init {
        attributes["src"] = src
    }
}