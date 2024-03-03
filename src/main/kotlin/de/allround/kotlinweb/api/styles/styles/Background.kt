package de.allround.kotlinweb.api.styles.styles

import de.allround.kotlinweb.api.styles.Style
import java.awt.Color
import java.net.URI

data class Background(
    val color: Color, val image: URI, val repeat: String, val attachment: String, val position: String
) : Style("background", "$color url(${image}) $repeat $attachment $position")