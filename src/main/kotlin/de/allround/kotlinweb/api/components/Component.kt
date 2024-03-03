package de.allround.kotlinweb.api.components

import de.allround.kotlinweb.api.action.Action
import de.allround.kotlinweb.api.action.HtmxAction
import de.allround.kotlinweb.api.action.ScriptAction
import de.allround.kotlinweb.api.action.misc.Trigger
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


    init {
        init?.let { it() }
    }

    fun buildStylesheet(): Stylesheet = Stylesheet(styles = styles)

    override fun toString(): String {
        //actions
        actions.forEach { it.apply(this) }

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
        val br = Component(type = "br", single = true)
        init?.let { br.it() }
        children.add(br)
        return br
    }

    fun newLine(init: (Component.() -> Unit) = {}): Component {
        return br(init = init)
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
        }
        val component = Component(type = actualString)
        init?.let { component.it() }
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

    fun htmx(trigger: Trigger, init: (HtmxAction.(Component) -> Unit) = {}) {
        val action = HtmxAction(trigger) {
            this.init(it)
        }
        actions.add(action)
    }

    fun script(trigger: Trigger, init: (ScriptAction.() -> Unit) = {}) {
        val action = ScriptAction(trigger) {
            this.init()
        }
        actions.add(action)
    }
}



