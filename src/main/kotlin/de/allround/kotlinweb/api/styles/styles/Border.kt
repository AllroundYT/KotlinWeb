package de.allround.kotlinweb.api.styles.styles

import de.allround.kotlinweb.api.styles.Style
import java.awt.Color

data class Border(
    val width: String, val style: BorderStyle, val color: Color
) : Style("border", "$width ${style.name.lowercase()} rgb(${color.red},${color.green},${color.blue})")
