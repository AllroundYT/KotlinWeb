package de.allround.kotlinweb.api.components.misc

import de.allround.kotlinweb.api.components.Component

data class MediaAttributes(
    val src: String,
    val alt: String? = null,
    val autoplay: Boolean? = null,
    val controls: Boolean? = null,
    val loop: Boolean? = null,
    val muted: Boolean? = null,
    val preload: String? = null,
) {
    fun apply(component: Component) {
        component.attributes.add("src", src)
        alt?.let { component.attributes.add("alt", it) }
        autoplay?.let {
            if (it) {
                component.attributes.add("autoplay", "")
            }
        }
        controls?.let {
            if (it) {
                component.attributes.add("controls", "")
            }
        }
        loop?.let {
            if (it) {
                component.attributes.add("loop", "")
            }
        }
        muted?.let {
            if (it) {
                component.attributes.add("muted", "")
            }
        }
        preload?.let { component.attributes.add("preload", it) }
    }
}
