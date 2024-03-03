package de.allround.kotlinweb.api.action.script

import de.allround.kotlinweb.api.action.AbstractScriptAction
import de.allround.kotlinweb.api.action.ScriptAction
import de.allround.kotlinweb.api.action.misc.Trigger
import de.allround.kotlinweb.api.components.Component

class ClassAction {
    class Add(private val clazz: String, trigger: Trigger) : AbstractScriptAction(trigger) {
        override fun build(component: Component): String = "add .$clazz to me "
    }

    class Remove(private val clazz: String, trigger: Trigger) : AbstractScriptAction(trigger) {
        override fun build(component: Component): String = "remove .$clazz from me "
    }

    class Toggle(private val clazz: String, trigger: Trigger) : AbstractScriptAction(trigger) {
        override fun build(component: Component): String = "toggle .$clazz on me "
    }
}
