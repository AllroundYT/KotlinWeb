package de.allround.kotlinweb.api.styles

import java.awt.Color
import java.awt.Font
import java.net.URI
import java.net.URL

open class Style(
    val name: String, vararg val value: Any
) {

    override fun toString(): String {
        val values = value.map {
            when (it) {
                is Number -> it
                is Color -> rgb(it)
                is Enum<*> -> it.name.replace("-", "").lowercase()
                is URL -> "url($it)"
                is URI -> "url(${it.toURL()})"
                is Font -> "'${it.fontName}', ${it.family}"
                else -> it
            }
        }.toList()
        val valueStringBuilder = StringBuilder()
        values.forEach {
            if (valueStringBuilder.isNotEmpty()) valueStringBuilder.append(" ")
            valueStringBuilder.append(it)
        }
        return "$name: $valueStringBuilder;"
    }

}