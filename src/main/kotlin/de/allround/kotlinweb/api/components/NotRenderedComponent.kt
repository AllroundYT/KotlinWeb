package de.allround.kotlinweb.api.components

class NotRenderedComponent: Component(type = "") {
    override fun toString(): String {
        val builder = StringBuilder()
        children.forEach {
            builder.append(it.toString()).append("\n")
        }
        return builder.toString()
    }
}