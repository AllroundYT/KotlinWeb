package de.allround.kotlinweb.api.styles.styles

import de.allround.kotlinweb.api.styles.Style
import java.awt.Color

data class Shadow(
    val hOffset: String, val vOffset: String, val blur: String, val spread: String, val color: Color
) : Style("box-shadow", "$hOffset $vOffset $blur $spread rgb(${color.red},${color.green},${color.blue})")
