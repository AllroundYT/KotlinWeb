package de.allround.kotlinweb.api.components

import de.allround.kotlinweb.api.action.Action
import de.allround.kotlinweb.api.action.trigger.Trigger
import de.allround.kotlinweb.api.styles.Stylesheet
import de.allround.kotlinweb.api.styles.Styling


open class Component(
    var id: String? = null,
    var classes: MutableList<String> = mutableListOf(),
    var children: MutableList<Component> = mutableListOf(),
    val type: String,
    var single: Boolean = false,
    var attributes: MutableMap<String, String> = mutableMapOf(),
    var content: Any? = null,
    var styles: MutableList<Styling> = mutableListOf(),
    val actions: MutableList<Action> = mutableListOf(),
    val init: (Component.() -> Unit)? = null,
) {

    fun addAttribute(key: String, value: String, separator: String = " ") {
        if (attributes[key] == null) {
            attributes[key] = value
        } else{
            attributes[key] += "$separator$value"
        }
    }

    init {
        init?.let { it() }
    }

    fun buildStylesheet(): Stylesheet {
        val stylesheet = Stylesheet(styles = styles)
        children.forEach {
            stylesheet.append(it.buildStylesheet())
        }
        return stylesheet
    }

    override fun toString(): String {
        //actions
        actions.forEach {
            it.apply()
        }

        //classes
        var classAttribute = attributes["class"] ?: ""
        classes.forEach { classAttribute += " $it" }
        attributes["class"] = classAttribute.trim()

        //id
        id?.let { attributes["id"] = it }

        //open tag
        val entryTagBuilder = StringBuilder("<")
        entryTagBuilder.append(type)

        //add attributes to entry tag
        val attributeBuilder = StringBuilder()
        attributes.forEach { (key, value) ->
            if (value.isEmpty()) return@forEach
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
        content?.let { contentBuilder.append(it) }
        //include child elements
        if (children.isNotEmpty()) {
            if (contentBuilder.isNotEmpty()) contentBuilder.append("\n")
            val childrenBuilder = StringBuilder()
            children.forEach {
                childrenBuilder.append("\n$it")
            }
            contentBuilder.append(childrenBuilder.toString())
        }

        return "$entryTagBuilder$contentBuilder</$type>"
    }

    fun div(init: Component.() -> Unit): Component {
        val component = Component(type = "div")
        component.init()
        children.add(component)
        return component
    }


    fun br(init: (Component.() -> Unit) = {}): Component {
        val component = Component(type = "br", single = true)
        init.let { component.it() }
        children.add(component)
        return component
    }

    fun linebreak(init: (Component.() -> Unit) = {}): Component {
        val component = Component(type = "br", single = true)
        init.let { component.it() }
        children.add(component)
        return component
    }

    fun script(src: String? = null, content: String? = null, init: (Component.() -> Unit) = {}): Component {
        val component = Component(type = "script")
        init.let { component.it() }
        if (src != null) {
            component.attributes["src"] = src
        } else if (content != null) {
            component.content = content
        } else {
            return component
        }
        children.add(component)
        return component
    }

    fun isOfType(type: String): Boolean {
        return type == this.type
    }

    fun text(type: TextType = TextType.TEXT, href: String? = null, text: String, init: (Component.() -> Unit) = {}): Component {
        val actualString = when (type) {
            TextType.H1 -> "h1"
            TextType.H2 -> "h2"
            TextType.H3 -> "h3"
            TextType.H4 -> "h4"
            TextType.H5 -> "h5"
            TextType.H6 -> "h6"
            TextType.TEXT -> "p"
            TextType.LINK -> "a"
            TextType.SPAN -> "span"
        }
        val component = Component(type = actualString)
        init.let { component.it() }
        if (type == TextType.LINK && href != null) {
            component.attributes["href"] = href
        }
        component.content = text
        children.add(component)
        return component
    }


    fun input(
        type: InputType,
        name: String,
        value: String? = null,
        placeholder: String? = null,
        init: (Component.() -> Unit) = {}
    ): Component {
        val component = Component(
            type = when (type) {
                InputType.TEXT_AREA -> "textarea"
                InputType.SELECT -> "select"
                InputType.OPTION -> "option"
                else -> "input"
            }
        )

        component.init()
        component.attributes["name"] = name
        if (placeholder != null) component.attributes["placeholder"] = placeholder
        if (value != null) component.attributes["value"] = value
        children.add(component)
        return component
    }

    fun option(name: String, value: String, init: (Component.() -> Unit) = {}): Component {
        return input(type = InputType.OPTION, name = name, value = value, init = init)
    }

    fun button(text: String, init: (Component.() -> Unit) = {}): Component {
        val component = Component(type = "button", content = text)
        component.init()
        children.add(component)
        return component
    }

    open fun on(trigger: Trigger, init: (Action.(Component) -> Unit) = {}) {
        val action = Action(Action.Type.NONE,this,trigger)
        action.init(this)
        actions.add(action)
    }

    fun style(selector: String, init: Styling.() -> Unit) {
        val styling = Styling(selector)
        styling.init()
        styles.add(styling)
    }
}



