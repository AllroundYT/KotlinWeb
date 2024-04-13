package de.allround.kotlinweb.api.action

import de.allround.kotlinweb.api.action.htmx.RequestAction
import de.allround.kotlinweb.api.action.trigger.Trigger
import de.allround.kotlinweb.api.action.hyperscript.ClassAction
import de.allround.kotlinweb.api.action.hyperscript.ElementAction
import de.allround.kotlinweb.api.action.hyperscript.WaitAction
import de.allround.kotlinweb.api.action.htmx.server.ServerAction
import de.allround.kotlinweb.api.action.htmx.server.ServerActionImpl
import de.allround.kotlinweb.api.action.htmx.server.ServerActions
import de.allround.kotlinweb.api.action.hyperscript.GoToUrlAction
import de.allround.kotlinweb.api.components.Component
import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.RoutingContext
import kotlin.time.Duration

open class Action(
    val type: Type,
    val component: Component,
    open val trigger: Trigger
) {

    enum class Type {
        HTMX, HYPERSCRIPT, NONE
    }

    private val chainedActions: MutableList<Action> = mutableListOf()
    private val scriptAttributeBuilder = StringBuilder()
    open fun build(){}

    fun apply() {
        chainedActions.forEach {
            apply(it)
        }

        if (scriptAttributeBuilder.isEmpty()) return
        if (component.attributes["_"] == null) {
            component.attributes["_"] = "$scriptAttributeBuilder"
        } else {
            component.attributes["_"] += "end $scriptAttributeBuilder"
        }

    }

    private fun apply(action: Action) {
        applyHtmx(action)
        applyHyperScript(action)
        action.chainedActions.forEach {
            applyHtmx(it)
            applyHyperScript(it)
        }
    }
    private fun applyHtmx(action: Action){
        if (action.type != Type.HTMX) return
        action.build()
    }
    private fun applyHyperScript(action: Action) {
        if (action.type != Type.HYPERSCRIPT) return
        if (scriptAttributeBuilder.isEmpty()) scriptAttributeBuilder.append(trigger.asScriptTrigger())
        else scriptAttributeBuilder.append("then ")
        scriptAttributeBuilder.append(action.toString())
    }

    fun serverAction(serverAction: ServerAction, route: String = "/") {
        val action = ServerActionImpl(component = component, trigger = trigger, action = serverAction, route = route)
        chainedActions.add(action)
    }

    fun gotoUrl(url: String, newWindow: Boolean = false) {
        val action = GoToUrlAction(route = url, newWindow = newWindow, component = component, trigger = trigger)
        chainedActions.add(action)
    }

    fun wait(duration: Duration, init: WaitAction.() -> Unit = {}) {
        val action = WaitAction(duration = duration, component = component, trigger = trigger)
        action.init()
        chainedActions.add(action)
    }

    fun remove(target: String = "me") {
        chainedActions.add(ElementAction.Remove(target, component, trigger))
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


    fun setValue(value: Any, target: String = "me") {
        chainedActions.add(ElementAction.Set(target = target, targetAttribute = "value", value = value, component = component, trigger = trigger))
    }
    fun setInnerHTML(value: Any, target: String = "me") {
        chainedActions.add(ElementAction.Set(target = target, targetAttribute = "innerHTML", value = value, component = component, trigger = trigger))
    }

    fun addClass(clazz: String, target: String = "me") {
        chainedActions.add(ClassAction.Add(clazz = clazz, target = target,component = component, trigger = trigger))
    }

    fun removeClass(clazz: String, target: String = "me") {
        chainedActions.add(ClassAction.Remove(clazz = clazz, target = target,component = component, trigger = trigger))
    }

    fun toggleClass(clazz: String, target: String = "me") {
        chainedActions.add(ClassAction.Toggle(clazz = clazz, target = target,component = component, trigger = trigger))
    }
}