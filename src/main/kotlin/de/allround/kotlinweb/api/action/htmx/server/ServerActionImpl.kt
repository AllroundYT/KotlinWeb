package de.allround.kotlinweb.api.action.htmx.server

import de.allround.kotlinweb.api.action.Action
import de.allround.kotlinweb.api.action.trigger.Trigger
import de.allround.kotlinweb.api.html.BaseComponent
import de.allround.kotlinweb.api.html.Component

class ServerActionImpl(private val action: ServerAction, val route: String = "/", component: Component<*>, trigger: Trigger) : Action(
    Type.HTMX, component, trigger
) {
    override fun build() {
        val component = BaseComponent(type = "div") {
            attributes["hx-headers"] = "{\"KW-SERVER-ACTION\": \"${action.id}\"}"
            attributes["hx-post"] = route
            attributes["hx-trigger"] = "${trigger.asHtmxTrigger()} from:closest ${component.type}"
            attributes["hx-swap"] = "none"
            if (action.include.isNotEmpty()) {
                val includeBuilder = StringBuilder()
                action.include.forEach {
                    if (includeBuilder.isNotEmpty()) includeBuilder.append(" ")
                    includeBuilder.append(it)
                }
                attributes["hx-include"] = includeBuilder.toString()
            }
        }
        this.component.add(component)
    }
}