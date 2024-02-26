package de.allround.kotlinweb.api.components.misc

enum class FormMethods {
    POST,GET;

    override fun toString(): String {
        return name.lowercase()
    }
}