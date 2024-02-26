package de.allround.kotlinweb.api.components.misc

import java.awt.Color
import java.util.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

data class Border(val color: Color, val size: Size)

data class Size(val value: Int, val type: String)

val Int.px: Size
    get() = Size(value = this, type = "px")

val Int.em: Size
    get() = Size(value = this, type = "em")

val Int.percent: Size
    get() = Size(value = this, type = "%")

val Int.cm: Size
    get() = Size(value = this, type = "cm")

class Stylesheet(val selector: String) {
    private val properties = mutableMapOf<String, Any>()

    fun color(color: Color) {
        properties["color"] = color
    }

    fun border(border: Border) {
        properties["border"] = border
    }

    fun margin(size: Size) {
        properties["margin"] = size
    }

    fun backgroundColor(color: Color) {
        properties["background-color"] = color
    }

    fun fontSize(size: Size) {
        properties["font-size"] = size
    }

    fun fontFamily(font: String) {
        properties["font-family"] = font
    }

    fun padding(size: Size) {
        properties["padding"] = size
    }

    fun textColor(color: Color) {
        properties["text-color"] = color
    }

    data class BoxShadow(val offsetX: Size, val offsetY: Size, val blurRadius: Size, val color: Color)

    fun boxShadow(boxShadow: BoxShadow) {
        properties["box-shadow"] = boxShadow
    }

    enum class TextAlign {
        LEFT, RIGHT, CENTER, JUSTIFY
    }

    fun textAlign(align: TextAlign) {
        properties["text-align"] = align
    }

    fun width(size: Size) {
        properties["width"] = size
    }

    fun height(size: Size) {
        properties["height"] = size
    }

    enum class Position {
        STATIC, RELATIVE, ABSOLUTE, FIXED
    }

    fun position(position: Position) {
        properties["position"] = position
    }

    fun top(size: Size) {
        properties["top"] = size
    }

    fun bottom(size: Size) {
        properties["bottom"] = size
    }

    fun left(size: Size) {
        properties["left"] = size
    }

    fun right(size: Size) {
        properties["right"] = size
    }

    fun zIndex(value: Int) {
        properties["z-index"] = value
    }

    enum class Display {
        INLINE, BLOCK, INLINE_BLOCK, FLEX, GRID
    }

    fun display(display: Display) {
        properties["display"] = display
    }

    enum class Overflow {
        VISIBLE, HIDDEN, SCROLL, AUTO
    }

    fun overflow(overflow: Overflow) {
        properties["overflow"] = overflow
    }

    data class Animation(
        val name: String,
        val duration: Duration,
        val timingFunction: String = "linear",
        val delay: Duration = 0.seconds,
        val iterationCount: Int = 1,
        val fillMode: String? = null,
        val keyframes: List<Stylesheet> = listOf()
    )

    fun animation(animation: Animation) {
        properties["animation"] = animation
    }

    data class Flexbox(
        val flexDirection: FlexDirection,
        val justifyContent: JustifyContent,
        val alignItems: AlignItems,
        val flexWrap: FlexWrap
    )

    enum class FlexDirection {
        ROW, ROW_REVERSE, COLUMN, COLUMN_REVERSE
    }

    enum class JustifyContent {
        FLEX_START, FLEX_END, CENTER, SPACE_BETWEEN, SPACE_AROUND
    }

    enum class AlignItems {
        FLEX_START, FLEX_END, CENTER, BASELINE, STRETCH
    }

    enum class FlexWrap {
        NOWRAP, WRAP, WRAP_REVERSE
    }

    fun flexbox(flexbox: Flexbox) {
        properties["flexbox"] = flexbox
    }

    data class LinearGradient(
        val angle: Int, val colors: List<Color>
    )

    fun linearGradient(linearGradient: LinearGradient) {
        properties["linear-gradient"] = linearGradient
    }

    data class RadialGradient(
        val shape: RadialShape, val colors: List<Color>
    )

    enum class RadialShape {
        CIRCLE, ELLIPSE
    }

    fun radialGradient(radialGradient: RadialGradient) {
        properties["radial-gradient"] = radialGradient
    }

    enum class TextDecoration {
        NONE, UNDERLINE, OVERLINE, LINE_THROUGH
    }

    fun textDecoration(textDecoration: TextDecoration) {
        properties["text-decoration"] = textDecoration
    }

    fun textDecorationColor(color: Color) {
        properties["text-decoration-color"] = color
    }

    fun textDecorationStyle(style: TextDecorationStyle) {
        properties["text-decoration-style"] = style
    }

    enum class TextDecorationStyle {
        SOLID, DOUBLE, DOTTED, DASHED, WAVY
    }

    fun generateCss(): String {
        val cssProperties = properties.entries.joinToString(separator = ";\n") { (key, value) ->
            when (value) {
                is Color -> "$key: rgba(${value.red}, ${value.green}, ${value.blue}, ${value.alpha})"
                is Size -> "$key: ${value.value}px"
                is Border -> "border: ${value.size.value}px solid rgba(${value.color.red}, ${value.color.green}, ${value.color.blue}, ${value.color.alpha})"
                is BoxShadow -> "box-shadow: ${value.offsetX.value}px ${value.offsetY.value}px ${value.blurRadius.value}px rgba(${value.color.red}, ${value.color.green}, ${value.color.blue}, ${value.color.alpha})"
                is Animation -> {
                    val keyFrames = value.keyframes

                    val builder = StringBuilder()

                    builder.append("animation: ${value.name} ${value.duration.inWholeSeconds}s ${value.timingFunction} ${value.delay.inWholeSeconds}s ${value.iterationCount}")
                    builder.append(System.lineSeparator())
                    builder.append("animation-fill-mode: ${value.fillMode}; ")
                    if (keyFrames.isNotEmpty()) {
                        builder.append("@keyframes ${value.name} {")
                        keyFrames.forEach {
                            builder.append(it.generateCss())
                        }
                        builder.append("}")
                    }

                    builder.toString()
                }

                is Flexbox -> "flex-direction: ${value.flexDirection.name.lowercase(Locale.getDefault())}; justify-content: ${
                    value.justifyContent.name.lowercase(
                        Locale.getDefault()
                    )
                }; align-items: ${
                    value.alignItems.name.lowercase(
                        Locale.getDefault()
                    )
                }; flex-wrap: ${
                    value.flexWrap.name.lowercase(
                        Locale.getDefault()
                    )
                }"

                is LinearGradient -> "background: linear-gradient(${value.angle}deg, ${value.colors.joinToString { "rgba(${it.red}, ${it.green}, ${it.blue}, ${it.alpha})" }})"
                is RadialGradient -> "background: radial-gradient(${value.shape.name.lowercase(Locale.getDefault())}, ${value.colors.joinToString { "rgba(${it.red}, ${it.green}, ${it.blue}, ${it.alpha})" }})"
                else -> "$key: $value"
            }
        }
        return "$selector {\n$cssProperties;\n}"
    }

    override fun toString(): String {
        return "Stylesheet(selector='$selector', properties=$properties)"
    }
}

fun stylesheet(selector: String, block: Stylesheet.() -> Unit): Stylesheet {
    return Stylesheet(selector).apply(block)
}
