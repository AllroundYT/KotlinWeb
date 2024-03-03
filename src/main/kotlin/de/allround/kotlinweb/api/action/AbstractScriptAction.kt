package de.allround.kotlinweb.api.action

import de.allround.kotlinweb.api.action.misc.Trigger
import de.allround.kotlinweb.api.action.script.ClassAction
import de.allround.kotlinweb.api.components.Component

abstract class AbstractScriptAction(trigger: Trigger) : Action(trigger) {

    abstract fun build(component: Component): String
    override fun apply(component: Component) {
        var attribute = component.attributes["_"]

        if (attribute == null) {
            attribute = build(component)
        } else {
            attribute += " end ${build(component)}"
        }
        component.attributes["_"] = attribute
    }
}