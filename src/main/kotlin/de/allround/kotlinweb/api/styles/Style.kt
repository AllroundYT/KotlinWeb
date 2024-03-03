package de.allround.kotlinweb.api.styles

import java.awt.Color

open class Style(
    val name: String, val value: Any
) {

    override fun toString(): String {
        return when (value) {
            is Number -> "$name: ${value};"
            is Color -> "$name: rgb(${value.red},${value.green},${value.blue});"
            is Enum<*> -> "$name: ${value.name.lowercase()};"
            else -> "$name: ${value};"
        }
    }
}