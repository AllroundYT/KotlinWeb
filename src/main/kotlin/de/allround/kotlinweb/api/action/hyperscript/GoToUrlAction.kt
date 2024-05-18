package de.allround.kotlinweb.api.action.hyperscript

import de.allround.kotlinweb.api.action.Action
import de.allround.kotlinweb.api.action.trigger.Trigger
import de.allround.kotlinweb.api.html.Component

class GoToUrlAction(val route: String, val newWindow: Boolean = false, component: Component<*>, trigger: Trigger) : Action(type = Type.HYPERSCRIPT, component,
    trigger
) {
    override fun toString(): String = "go to url $route ${if (newWindow) "in new window " else ""}"
}