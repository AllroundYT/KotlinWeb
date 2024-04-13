package de.allround.kotlinweb.api.components.common

import de.allround.kotlinweb.api.components.Component
import de.allround.kotlinweb.api.components.misc.TextType

class Text(val variant: TextType, val text: String, val init: Text.() -> Unit = {}) : Component(type = variant.name.lowercase(), onInit = {
    init.invoke(this as Text)
    innerHTML = text
})
