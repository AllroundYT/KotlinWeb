package de.allround.kotlinweb.api.action.hyperscript

import de.allround.kotlinweb.api.action.Action
import de.allround.kotlinweb.api.action.trigger.Trigger
import de.allround.kotlinweb.api.html.Component

class ElementAction {
    class Remove(val target: String, component: Component<*>, trigger: Trigger) : Action(
        type = Type.HYPERSCRIPT,
        trigger = trigger,
        component = component
    ) {
        override fun toString(): String {
            return "remove $target "
        }
    }

    class Set(
        val target: String,val targetAttribute: String, val value: Any, component: Component<*>, trigger: Trigger
    ) : Action(type = Type.HYPERSCRIPT, component = component, trigger = trigger) {

        override fun toString(): String {
            return "set $targetAttribute of $target to $value "
        }
    }
}