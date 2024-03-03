package de.allround.kotlinweb.api.styles.styles

import de.allround.kotlinweb.api.styles.Style

data class Font(
    val family: String,
    val size: String,
    val weight: FontWeight,
    val style: FontStyle,
    val variant: String,
    val lineHeight: String
) : Style("font", "$style $variant $weight $size/$lineHeight $family")