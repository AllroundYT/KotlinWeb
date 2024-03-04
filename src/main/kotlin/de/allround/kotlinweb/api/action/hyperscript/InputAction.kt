package de.allround.kotlinweb.api.action.hyperscript

import de.allround.kotlinweb.api.action.Action
import de.allround.kotlinweb.api.action.trigger.Trigger
import de.allround.kotlinweb.api.components.Component

class InputAction {

    class SetValue(
        val target: String, val value: Any, component: Component, trigger: Trigger
    ) : Action(type = Type.HYPERSCRIPT, component = component, trigger = trigger) {

        override fun toString(): String {
            return "set $target.value to '$value'"
        }
    }
}