package de.allround.kotlinweb.api.components.misc

enum class MediaType {
    IMAGE,
    VIDEO;

    override fun toString(): String {
        return name.lowercase()
    }
}