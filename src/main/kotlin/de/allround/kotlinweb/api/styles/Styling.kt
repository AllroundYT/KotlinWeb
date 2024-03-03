package de.allround.kotlinweb.api.styles

class Styling(val selector: String, val styles: MutableList<Style> = mutableListOf()) {
    fun append(style: Style) {
        styles.add(style)
    }

    override fun toString(): String {
        val builder = StringBuilder()

        builder.append(selector).append(" {").append(System.lineSeparator())
        styles.forEach {
            builder.append(it.toString()).append(System.lineSeparator())
        }
        builder.append(" }")

        return builder.toString()
    }

    fun add(name: String, value: Any) {
        append(Style(name, value))
    }
}

