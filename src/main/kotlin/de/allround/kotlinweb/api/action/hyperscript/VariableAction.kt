package de.allround.kotlinweb.api.action.hyperscript

import de.allround.kotlinweb.api.action.Action
import de.allround.kotlinweb.api.action.trigger.Trigger
import de.allround.kotlinweb.api.html.Component

class VariableAction {
    class Set(component: Component<*>, trigger: Trigger, val name: String, val value: Any) : Action(
        type = Type.HYPERSCRIPT, component = component, trigger = trigger
    ) {
        override fun toString(): String =
            "set $name to $value "
    }

    class Increment(component: Component<*>, trigger: Trigger, val name: String) : Action(
        type = Type.HYPERSCRIPT, component = component, trigger = trigger
    ) {
        override fun toString(): String =
            "increment $name "
    }

    class Decrement(component: Component<*>, trigger: Trigger, val name: String) : Action(
        type = Type.HYPERSCRIPT, component = component, trigger = trigger
    ) {
        override fun toString(): String =
            "decrement $name "
    }
}