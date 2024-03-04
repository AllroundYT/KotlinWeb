package de.allround.kotlinweb.api.action

import de.allround.kotlinweb.api.action.htmx.RequestAction
import de.allround.kotlinweb.api.action.trigger.Trigger
import de.allround.kotlinweb.api.action.hyperscript.ClassAction
import de.allround.kotlinweb.api.action.hyperscript.InputAction
import de.allround.kotlinweb.api.components.Component
import io.vertx.core.http.HttpMethod

open class Action(
    val type: Type,
    val component: Component,
    open val trigger: Trigger
) {

    enum class Type {
        HTMX, HYPERSCRIPT, NONE
    }

    private val chainedActions: MutableList<Action> = mutableListOf()
    open fun build(){}

    fun apply() {
        chainedActions.forEach {
            when (it.type) {
                Type.HTMX -> {
                    applyHtmx()
                }
                Type.HYPERSCRIPT -> {
                    applyHyperScript()
                }
                Type.NONE -> {}
            }
        }
    }
    private fun applyHtmx(){
        chainedActions.forEach {
            it.build()
        }
    }
    private fun applyHyperScript() {
        val attributeBuilder = StringBuilder()

        chainedActions.forEach {
            if (attributeBuilder.isEmpty()) attributeBuilder.append(trigger.asScriptTrigger())
            else attributeBuilder.append("then ")
            attributeBuilder.append("$it ")
        }

        if (component.attributes["_"] == null) {
            component.attributes["_"] = "$attributeBuilder"
        } else {
            component.attributes["_"] += "end $attributeBuilder"
        }
    }

    fun get(route: String, init: RequestAction.() -> Unit = {}) {
        request(HttpMethod.GET, route, init)
    }

    fun post(route: String, init: RequestAction.() -> Unit = {}) {
        request(HttpMethod.POST, route, init)
    }

    fun put(route: String, init: RequestAction.() -> Unit = {}) {
        request(HttpMethod.PUT, route, init)
    }

    fun delete(route: String, init: RequestAction.() -> Unit = {}) {
        request(HttpMethod.DELETE, route, init)
    }

    fun request(httpMethod: HttpMethod,route: String, init: RequestAction.() -> Unit = {}) {
        val action = RequestAction(
            component = component,
            trigger = trigger,
            method = httpMethod,
            route = route
        )
        action.init()
        chainedActions.add(action)
    }

    fun setValue(value: Any, target: String) {
        chainedActions.add(InputAction.SetValue(target, value, component, trigger))
    }

    fun addClass(clazz: String) {
        chainedActions.add(ClassAction.Add(clazz, component, trigger))
    }

    fun removeClass(clazz: String) {
        chainedActions.add(ClassAction.Remove(clazz, component, trigger))
    }

    fun toggleClass(clazz: String) {
        chainedActions.add(ClassAction.Toggle(clazz, component, trigger))
    }
}