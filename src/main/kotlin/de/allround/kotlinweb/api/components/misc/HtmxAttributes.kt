package de.allround.kotlinweb.api.components.misc

import de.allround.kotlinweb.api.components.Component
import java.util.*

data class HtmxAttributes(
    val get: String? = null,
    val post: String? = null,
    val put: String? = null,
    val delete: String? = null,
    val patch: String? = null,
    val trigger: String? = null,
    val target: String? = null,
    val swap: String? = null,
    val push: Boolean? = null,
    val history: String? = null,
    val indicator: String? = null,
    val confirm: String? = null,
    val prompt: String? = null,
    val select: String? = null,
    val include: String? = null,
    val includedComponents: List<Component>? = null,
    val vars: String? = null,
    val headers: String? = null,
    val timeout: String? = null,
    val boost: Boolean? = null,
    val removeMe: String? = null
) {
    fun apply(component: Component) {
        get?.let { component.attributes.add("hx-get", it) }
        post?.let { component.attributes.add("hx-post", it) }
        put?.let { component.attributes.add("hx-put", it) }
        delete?.let { component.attributes.add("hx-delete", it) }
        patch?.let { component.attributes.add("hx-patch", it) }
        trigger?.let { component.attributes.add("hx-trigger", it) }
        target?.let { component.attributes.add("hx-target", it) }
        swap?.let { component.attributes.add("hx-swap", it) }
        push?.let { component.attributes.add("hx-push-url", it) }
        history?.let { component.attributes.add("hx-history", it) }
        indicator?.let { component.attributes.add("hx-indicator", it) }
        confirm?.let { component.attributes.add("hx-confirm", it) }
        prompt?.let { component.attributes.add("hx-prompt", it) }
        select?.let { component.attributes.add("hx-select", it) }
        include?.let { component.attributes.add("hx-include", it) }
        includedComponents?.let { components ->
            val builder = StringBuilder()
            components.forEach {
                if (it.id == null){
                    it.id = UUID.randomUUID().toString().replace("-","")
                }
                if (builder.isNotEmpty()) builder.append(", ")
                builder.append("#${it.id}")
            }

            component.attributes.add("hx-include",builder.toString())
        }
        vars?.let { component.attributes.add("hx-vars", it) }
        headers?.let { component.attributes.add("hx-headers", it) }
        timeout?.let { component.attributes.add("hx-timeout", it) }
        boost?.let { component.attributes.add("hx-boost", it.toString()) }
        removeMe?.let {component.attributes.add("remove-me", it)}
    }
}
