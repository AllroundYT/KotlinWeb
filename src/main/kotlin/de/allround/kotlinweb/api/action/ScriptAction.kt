package de.allround.kotlinweb.api.action

import de.allround.kotlinweb.api.action.misc.Trigger
import de.allround.kotlinweb.api.action.script.ClassAction
import de.allround.kotlinweb.api.components.Component

class ScriptAction(trigger: Trigger, val init: ScriptAction.() -> Unit) : Action(trigger) {

    private val chainedActions: MutableList<AbstractScriptAction> = mutableListOf()

    override fun apply(component: Component) {
        init()
        val attributeBuilder = StringBuilder()

        chainedActions.forEach {
            if (attributeBuilder.isEmpty()) attributeBuilder.append(trigger.asScriptTrigger())
            else attributeBuilder.append("then ")
            attributeBuilder.append("${it.build(component)} ")
        }

        if (component.attributes["_"] == null) {
            component.attributes["_"] = "$attributeBuilder"
        } else{
            component.attributes["_"] +=  "end $attributeBuilder"
        }
    }

    fun addClass(clazz: String) {
        chainedActions.add(ClassAction.Add(clazz, trigger))
    }

    fun removeClass(clazz: String) {
        chainedActions.add(ClassAction.Remove(clazz, trigger))
    }

    fun toggleClass(clazz: String) {
        chainedActions.add(ClassAction.Toggle(clazz, trigger))
    }
}