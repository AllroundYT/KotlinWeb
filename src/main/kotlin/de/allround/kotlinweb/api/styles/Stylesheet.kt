package de.allround.kotlinweb.api.styles

import de.allround.kotlinweb.api.components.Component
import de.allround.kotlinweb.api.styles.styles.Styling

class Stylesheet(val styles: MutableList<Styling> = mutableListOf()) {

    fun containsStylesheet(other: Stylesheet): Boolean {
        // Durchlaufen Sie alle Stile im anderen Stylesheet
        for (otherStyling in other.styles) {
            // Finden Sie den entsprechenden Stil in diesem Stylesheet
            val thisStyling = this.styles.find { it.selector == otherStyling.selector }

            // Wenn der Stil nicht gefunden wurde, geben Sie false zurück
            if (thisStyling == null) {
                return false
            }

            // Überprüfen Sie, ob alle Stile im anderen Stil in diesem Stil enthalten sind
            for (otherStyle in otherStyling.styles) {
                var styleFound = false
                for (thisStyle in thisStyling.styles) {
                    if (thisStyle.name == otherStyle.name && thisStyle.value == otherStyle.value) {
                        styleFound = true
                        break
                    }
                }

                // Wenn der Stil nicht gefunden wurde, geben Sie false zurück
                if (!styleFound) {
                    return false
                }
            }
        }

        // Wenn alle Stile gefunden und übereinstimmen, geben Sie true zurück
        return true
    }

    override fun toString(): String {
        val builder = StringBuilder()

        val filteredStyles = mutableListOf<Styling>()
        styles.forEach { styling ->
            val selector = styling.selector
            val styles = styling.styles

            if (filteredStyles.any { it.selector == selector }) {
                filteredStyles.filter { it.selector == selector }.forEach {
                    styles.forEach { style ->
                        it.styles.removeIf { it.name == style.name }
                        it.styles.add(style)
                    }
                }
            } else {
                filteredStyles.add(styling)
            }
        }

        filteredStyles.forEach {
            builder.append("$it \n")
        }

        return builder.toString()
    }

    fun append(styling: Styling) {
        if (styles.any { it.selector == styling.selector }) {
            styles.filter { it.selector == styling.selector }.forEach {
                styling.styles.forEach { style ->
                    it.styles.removeIf { it.name == style.name }
                    it.styles.add(style)
                }
            }
        } else {
            styles.add(styling)
        }
    }

    fun append(stylesheet: Stylesheet) {
        stylesheet.styles.forEach { styling ->
            if (styles.any { it.selector == styling.selector }) {
                styles.filter { it.selector == styling.selector }.forEach {
                    styling.styles.forEach { style ->
                        it.styles.removeIf { it.name == style.name }
                        it.styles.add(style)
                    }
                }
            } else {
                styles.add(styling)
            }
        }
    }

    fun append(component: Component) = append(component.buildStylesheet())
}