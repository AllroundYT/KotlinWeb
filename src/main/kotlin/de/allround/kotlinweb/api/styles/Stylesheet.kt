package de.allround.kotlinweb.api.styles

import de.allround.kotlinweb.api.components.Component

class Stylesheet(val styles: MutableList<Styling> = mutableListOf()) {

    override fun toString(): String {
        val builder = StringBuilder()

        val filteredStyles = mutableListOf<Styling>()
        styles.forEach { styling ->
            val selector = styling.selector
            val styles = styling.styles

            if (filteredStyles.any { it.selector == selector }) {
                filteredStyles.filter { it.selector == selector }.forEach {
                    styles.forEach { style ->
                        it.styles.add(style)
                    }
                }
            } else {
                filteredStyles.add(styling)
            }
        }

        return builder.toString()
    }


    fun append(styling: Styling) = styles.add(styling)
    fun append(stylesheet: Stylesheet) = styles.addAll(stylesheet.styles)
    fun append(component: Component) = append(component.buildStylesheet())
}