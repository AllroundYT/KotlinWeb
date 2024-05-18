package de.allround.kotlinweb.api.action.hyperscript

import de.allround.kotlinweb.api.action.Action
import de.allround.kotlinweb.api.action.trigger.Trigger
import de.allround.kotlinweb.api.html.Component

class ClassAction {
    class Add(private val clazz: String, val target: String = "me", component: Component<*>, trigger: Trigger) : Action(
        type = Type.HYPERSCRIPT,
        component = component,
        trigger = trigger
    ) {
        override fun toString(): String = "add .$clazz to $target "
    }

    class Remove(private val clazz: String, val target: String = "me", component: Component<*>, trigger: Trigger) : Action(
        type = Type.HYPERSCRIPT,
        component = component,
        trigger = trigger
    ) {
        override fun toString(): String = "remove .$clazz from $target "
    }

    class Toggle(private val clazz: String, val target: String = "me", component: Component<*>, trigger: Trigger) : Action(
        type = Type.HYPERSCRIPT,
        component = component,
        trigger = trigger
    ) {
        override fun toString(): String = "toggle .$clazz on $target "
    }
}
