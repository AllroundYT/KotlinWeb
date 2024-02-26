package de.allround.kotlinweb.util

import org.apache.commons.text.StringEscapeUtils

object Util {
    val ESCAPE_HTML4: Function1<String, String> = { it ->
        StringEscapeUtils.escapeHtml4(it)
    }

    val KEBAB_CASE: Function1<String, String> = { it ->
        val builder = StringBuilder()
        it.forEach {
            if (it.isUpperCase() && builder.isNotEmpty()) builder.append("-")
            builder.append(it.lowercase())
        }

        builder.toString()
    }
}
