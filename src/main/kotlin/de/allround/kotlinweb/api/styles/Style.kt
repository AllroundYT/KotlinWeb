package de.allround.kotlinweb.api.styles

import java.awt.Color
import java.awt.Font
import java.net.URL

open class Style(
    val name: String, val value: Any, var inherit: Boolean = false, var unset: Boolean = false
) {

    override fun toString(): String {
        return "$name: ${
            when (value) {
                is Number -> value
                is Color -> rgb(value)
                is Enum<*> -> value.name.replace("-", "").lowercase()
                is URL -> "url($value)"
                is Font -> "'${value.fontName}', ${value.family}"
                else -> value
            }
        };"
    }

}