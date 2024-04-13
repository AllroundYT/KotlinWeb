package de.allround.kotlinweb.api.html

open class Page(
    val lang: String = "en",
    val head: Component,
    var body: Component
) : Component(type = "html") {

    init {
        attributes["lang"] = lang
        children.add(head)
        children.add(body)

        val metaTag = Component(type = "meta") {
            attributes["name"] = "viewport"
            attributes["content"] = "width=device-width, initial-scale=1"
        }
        head.addChild(metaTag)
    }

    override fun toString(): String {
        return  "<!DOCTYPE html>${super.toString()}"
    }
}

