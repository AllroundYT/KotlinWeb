package de.allround.kotlinweb.api.html

import de.allround.kotlinweb.api.action.Action
import de.allround.kotlinweb.api.action.trigger.Trigger
import de.allround.kotlinweb.util.Util.ESCAPE_HTML4
import java.util.*
import kotlin.collections.ArrayList


open class Component<T : Component<T>>(
    var id: String = "ID${UUID.randomUUID().toString().replace("-","")}",
    var classes: MutableSet<String> = mutableSetOf(),
    protected var children: MutableList<Component<*>> = mutableListOf(),
    val type: String,
    var single: Boolean = false,
    var attributes: MutableMap<String, String> = mutableMapOf(),
    var innerHTML: Any? = null,
    var renderAsChildren: Boolean = true,
    val componentInit: T.() -> Unit = {}
) {

    open fun init() {
        (this as T).componentInit()
    }

    val actions: MutableList<Action> = ArrayList()

    var parent: Component<*>? = null
        set(value) = if (parent != null) {
            throw IllegalStateException("Parent already set")
        } else {
            field = value
        }

    fun addAttribute(key: String, value: String, separator: String = " ") {
        if (attributes[key] == null) {
            attributes[key] = value
        } else {
            attributes[key] += "$separator$value"
        }
    }


    override fun toString(): String {

        init()


        actions.forEach {
            it.apply()
        }

        //classes
        var classAttribute = attributes["class"] ?: ""
        classes.forEach { classAttribute += " $it" }
        attributes["class"] = classAttribute.trim()

        //id
        id.let { attributes["id"] = it }

        //open tag
        val entryTagBuilder = StringBuilder("<")
        entryTagBuilder.append(type)

        //add attributes to entry tag
        val attributeBuilder = StringBuilder()
        attributes.forEach { (key, value) ->
            if (value.isEmpty()) {
                attributeBuilder.append(" $key")
                return@forEach
            }
            if (type == "html" && key != "lang") {
                return@forEach
            }
            attributeBuilder.append(" $key=\"$value\"")
        }
        entryTagBuilder.append(attributeBuilder.toString())

        //close if it's a single tag
        if (single) {
            entryTagBuilder.append(" />")
            return entryTagBuilder.toString()
        } else {
            entryTagBuilder.append(">")
        }

        //build content
        val contentBuilder = StringBuilder()
        //include content variable
        innerHTML?.let { contentBuilder.append(if (escapeHTML) ESCAPE_HTML4(it.toString()) else it) }
        //include child elements
        if (children.isNotEmpty()) {
            if (contentBuilder.isNotEmpty()) contentBuilder.append("\n")
            val childrenBuilder = StringBuilder()
            children.filter { it.renderAsChildren }.forEach {
                childrenBuilder.append("$it")
            }
            contentBuilder.append(childrenBuilder.toString())
        }
        return "$entryTagBuilder$contentBuilder</$type>"
    }

    var escapeHTML: Boolean = false

    fun addClasses(vararg classes: String) {
        this.classes.addAll(classes)
    }

    open fun <T : Component<T>> add(component: T): T {
        children.add(component)
        component.parent = this
        return component
    }

    open fun <T : Component<T>> first(component: T): T {
        children.addFirst(component)
        component.parent = this
        return component
    }

    open fun <T : Component<T>> last(component: T): T {
        children.addLast(component)
        component.parent = this
        return component
    }

    open fun on(trigger: Trigger, init: (Action.(T) -> Unit) = {}) {
        val action = Action(Action.Type.NONE,this,trigger)
        action.init(this as T)
        actions.add(action)
    }


}



