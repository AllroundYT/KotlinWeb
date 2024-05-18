package de.allround.kotlinweb.api.html.tags

import de.allround.kotlinweb.api.html.Component
import java.net.URI

class Link(val rel: String, val href: URI) : Component<Link>(type = "link") {
    init {
        attributes["rel"] = rel
        attributes["href"] = href.toString()
    }
}