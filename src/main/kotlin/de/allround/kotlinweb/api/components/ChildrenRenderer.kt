package de.allround.kotlinweb.api.components

class ChildrenRenderer: Component(type = "") {
    override fun toString(): String {
        val builder = StringBuilder()
        this.children.forEach {
            builder.append(it.toString()).append("\n")
        }
        return builder.toString()
    }
}