package de.allround.kotlinweb.api.html.tags

import de.allround.kotlinweb.api.html.Component

class MetaInfo(val name: String, val content: String) : Component<MetaInfo>(type = "meta") {
    init {
        attributes["name"] = name
        attributes["content"] = content
    }
}