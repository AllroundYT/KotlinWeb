package de.allround.kotlinweb.api.components.misc

import de.allround.kotlinweb.api.components.Component
import de.allround.kotlinweb.api.components.misc.Stylesheet
import java.awt.Color
import java.util.*

data class Styling(
    val selector: String? = null,
    var stylesheet: Stylesheet? = null,
    val global: Boolean = false,
    val color: Color? = null,
    val backgroundColor: Color? = null,
    val width: String? = null,
    val height: String? = null,
    val margin: String? = null,
    val padding: String? = null,
    val border: String? = null,
    val fontFamily: String? = null,
    val fontSize: String? = null,
    val fontStyle: String? = null,
    val fontWeight: String? = null,
    val lineHeight: Float? = null,
    val textAlign: String? = null,
    val textDecoration: String? = null,
    val textTransform: String? = null,
    val letterSpacing: String? = null,
    val wordSpacing: String? = null,
    val textIndent: String? = null,
    val textShadow: String? = null,
    val display: String? = null,
    val position: String? = null,
    val top: String? = null,
    val right: String? = null,
    val bottom: String? = null,
    val left: String? = null,
    val zIndex: Int? = null,
    val opacity: Float? = null,
    val transform: String? = null,
    val transition: String? = null,
    val animation: String? = null,
    val filter: String? = null,
    val backdropFilter: String? = null,
    val boxShadow: String? = null,
    val overflow: String? = null,
    val overflowX: String? = null,
    val overflowY: String? = null,
    val whiteSpace: String? = null,
    val cursor: String? = null,
    val pointerEvents: String? = null,
    val userSelect: String? = null,
    val resize: String? = null,
    val scrollBehavior: String? = null,
    val willChange: String? = null,
    val clipPath: String? = null
) {


    fun generateComponent(): Component {
        val component = Component(type = "style", content = generateStylesheet())
        return component
    }

    fun generateStylesheet(component: Component? = null): String {
        val builder = StringBuilder()


        stylesheet?.let { builder.append(it.generateCss()).append(" ") }

        if (global) {
            builder.append("* {")
        } else {
            if (selector == null && component != null){
                if (component.id == null) component.id = UUID.randomUUID().toString().lowercase().replace("-","")
                builder.append("#${component.id} {")
            } else{
                builder.append("$selector {")
            }
        }

        color?.let { builder.append("color: ${it.rgb}; ") }
        backgroundColor?.let { builder.append("background-color: ${it.rgb}; ") }
        width?.let { builder.append("width: ${it}px; ") }
        height?.let { builder.append("height: ${it}px; ") }
        margin?.let { builder.append("margin: ${it}px; ") }
        padding?.let { builder.append("padding: ${it}px; ") }
        border?.let { builder.append("border: $it; ") }
        fontFamily?.let { builder.append("font-family: '$it'; ") }
        fontSize?.let { builder.append("font-size: ${it}px; ") }
        fontStyle?.let { builder.append("font-style: $it; ") }
        fontWeight?.let { builder.append("font-weight: $it; ") }
        lineHeight?.let { builder.append("line-height: $it; ") }
        textAlign?.let { builder.append("text-align: $it; ") }
        textDecoration?.let { builder.append("text-decoration: $it; ") }
        textTransform?.let { builder.append("text-transform: $it; ") }
        letterSpacing?.let { builder.append("letter-spacing: ${it}px; ") }
        wordSpacing?.let { builder.append("word-spacing: ${it}px; ") }
        textIndent?.let { builder.append("text-indent: ${it}px; ") }
        textShadow?.let { builder.append("text-shadow: $it; ") }
        display?.let { builder.append("display: $it; ") }
        position?.let { builder.append("position: $it; ") }
        top?.let { builder.append("top: ${it}px; ") }
        right?.let { builder.append("right: ${it}px; ") }
        bottom?.let { builder.append("bottom: ${it}px; ") }
        left?.let { builder.append("left: ${it}px; ") }
        zIndex?.let { builder.append("z-index: $it; ") }
        opacity?.let { builder.append("opacity: $it; ") }
        transform?.let { builder.append("transform: $it; ") }
        transition?.let { builder.append("transition: $it; ") }
        animation?.let { builder.append("animation: $it; ") }
        filter?.let { builder.append("filter: $it; ") }
        backdropFilter?.let { builder.append("backdrop-filter: $it; ") }
        boxShadow?.let { builder.append("box-shadow: $it; ") }
        overflow?.let { builder.append("overflow: $it; ") }
        overflowX?.let { builder.append("overflow-x: $it; ") }
        overflowY?.let { builder.append("overflow-y: $it; ") }
        whiteSpace?.let { builder.append("white-space: $it; ") }
        cursor?.let { builder.append("cursor: $it; ") }
        pointerEvents?.let { builder.append("pointer-events: $it; ") }
        userSelect?.let { builder.append("user-select: $it; ") }
        resize?.let { builder.append("resize: $it; ") }
        scrollBehavior?.let { builder.append("scroll-behavior: $it; ") }
        willChange?.let { builder.append("will-change: $it; ") }
        clipPath?.let { builder.append("clip-path: $it; ") }


        builder.append("}")

        return builder.toString()
    }
}