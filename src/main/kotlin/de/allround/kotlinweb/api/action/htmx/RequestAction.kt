package de.allround.kotlinweb.api.action.htmx

import de.allround.kotlinweb.api.action.Action
import de.allround.kotlinweb.api.action.trigger.Trigger
import de.allround.kotlinweb.api.html.Component
import io.vertx.core.http.HttpMethod
import java.util.UUID

class RequestAction(
    component: Component<*>,
    trigger: Trigger,
    val route: String,
    val method: HttpMethod,
    var swapType: SwapType = SwapType.outerHTML,
    var target: String = "this",
    var boost: Boolean = false,
    var push: Boolean = false,
    var select: String? = null,
    var vals: String? = null,
    var confirm: String? = null,
    val include: MutableList<String> = mutableListOf()
) : Action(type = Type.HTMX, component = component, trigger = trigger) {
    override fun build() {
        val trigger = this.trigger.asHtmxTrigger()
        val attributes = component.attributes

        attributes["hx-trigger"] = trigger
        attributes["hx-target"] = target
        attributes["hx-swap"] = swapType.name
        attributes["hx-${method.name().lowercase()}"] = route
        if (push) {
            attributes["hx-push-url"] = "true"
        }
        confirm?.let {
            attributes["hx-confirm"] = it
        }
        select?.let {
            attributes["hx-select"] = it
        }
        vals?.let {
            attributes["hx-vals"] = it
        }
        if (boost) {
            attributes["hx-boost"] = "true"
        }
        if (include.isNotEmpty()) {
            val attributeBuilder = StringBuilder()
            include.forEach {
                if (attributeBuilder.isNotEmpty()) attributeBuilder.append(", ")
                attributeBuilder.append(it)
            }

            attributes["hx-include"] = attributeBuilder.toString()
        }
    }
}
