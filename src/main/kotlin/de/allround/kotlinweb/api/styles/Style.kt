package de.allround.kotlinweb.api.styles

import java.awt.Color
import java.awt.Font
import java.net.URI
import java.net.URL

open class Style(
    val name: String, val value: Any
) {

    override fun toString(): String {
        return "$name: ${
            when (value) {
                is Color -> rgb(value)
                is Enum<*> -> value.name.replace("-", "").lowercase()
                is URL -> "url($value)"
                is URI -> "url(${value.toURL()})"
                is Font -> "'${value.fontName}', ${value.family}"
                else -> value.toString()
            }
        };"
    }

}