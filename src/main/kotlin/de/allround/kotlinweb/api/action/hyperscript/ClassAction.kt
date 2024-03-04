package de.allround.kotlinweb.api.action.hyperscript

import de.allround.kotlinweb.api.action.Action
import de.allround.kotlinweb.api.action.trigger.Trigger
import de.allround.kotlinweb.api.components.Component

class ClassAction {
    class Add(private val clazz: String, component: Component, trigger: Trigger) : Action(
        type = Type.HYPERSCRIPT,
        component = component,
        trigger = trigger
    ) {
        override fun toString(): String = "add .$clazz to me "
    }

    class Remove(private val clazz: String, component: Component, trigger: Trigger) : Action(
        type = Type.HYPERSCRIPT,
        component = component,
        trigger = trigger
    ) {
        override fun toString(): String = "remove .$clazz from me "
    }

    class Toggle(private val clazz: String, component: Component, trigger: Trigger) : Action(
        type = Type.HYPERSCRIPT,
        component = component,
        trigger = trigger
    ) {
        override fun toString(): String = "toggle .$clazz on me "
    }
}
