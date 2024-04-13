package de.allround.kotlinweb.api.html

import de.allround.kotlinweb.util.Util.ESCAPE_HTML4
import java.util.*


open class Component(
    var id: String = "ID${UUID.randomUUID().toString().replace(" - ","")}",
    var classes: MutableSet<String> = mutableSetOf(),
    protected var children: MutableList<Component> = mutableListOf(),
    val type: String,
    var single: Boolean = false,
    var attributes: MutableMap<String, String> = mutableMapOf(),
    var innerHTML: Any? = null,
    var renderAsChildren: Boolean = true,
    onInit: (Component.() -> Unit)? = null,
) {

    var parent: Component? = null
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

    init {
        onInit?.let { it() }
    }


    override fun toString(): String {
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

    fun addChild(component: Component) {
        children.add(component)
        component.parent = this
    }

    fun addAsFirstChild(component: Component) {
        children.addFirst(component)
        component.parent = this
    }

    fun addAsLastChild(component: Component) {
        children.addLast(component)
        component.parent = this
    }
}



