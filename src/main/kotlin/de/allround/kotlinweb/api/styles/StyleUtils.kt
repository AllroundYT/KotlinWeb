package de.allround.kotlinweb.api.styles

import de.allround.kotlinweb.api.deg
import java.awt.Color

fun rgb(color: Color): String = "rgb(${color.red},${color.green},${color.blue})"
fun linearGradient(deg: Number, color1: Color, color2: Color): String =
    "linear-gradient(${deg.deg}, ${rgb(color1)}, ${rgb(color2)})"