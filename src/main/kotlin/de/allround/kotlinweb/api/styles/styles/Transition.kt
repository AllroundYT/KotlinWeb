package de.allround.kotlinweb.api.styles.styles

import de.allround.kotlinweb.api.styles.Style

data class Transition(
    val property: String, val duration: String, val timingFunction: String, val delay: String
) : Style("transition", "$property $duration $timingFunction $delay")