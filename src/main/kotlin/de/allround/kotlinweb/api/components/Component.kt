package de.allround.kotlinweb.api.components

import de.allround.kotlinweb.api.action.Action
import de.allround.kotlinweb.api.action.trigger.Trigger
import de.allround.kotlinweb.api.components.misc.FormMethod
import de.allround.kotlinweb.api.components.misc.InputType
import de.allround.kotlinweb.api.components.misc.TextType
import de.allround.kotlinweb.api.styles.Stylesheet
import de.allround.kotlinweb.api.styles.Styling
import de.allround.kotlinweb.util.Util
import de.allround.kotlinweb.util.Util.ESCAPE_HTML4
import java.net.URL
import java.util.UUID


open class Component(
    var id: String = "ID${UUID.randomUUID().toString().replace(" - ","")}",
    var classes: MutableSet<String> = mutableSetOf(),
    protected var children: MutableList<Component> = mutableListOf(),
    val type: String,
    var single: Boolean = false,
    var attributes: MutableMap<String, String> = mutableMapOf(),
    var innerHTML: Any? = null,
    var styles: MutableList<Styling> = mutableListOf(),
    val actions: MutableList<Action> = mutableListOf(),
    var renderAsChildren: Boolean = true,
    val onInit: (Component.() -> Unit)? = null,
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

    fun div(addAsChild: Boolean = true,init: Component.() -> Unit): Component {
        val component = Component(type = "div")
        component.init()
        if (addAsChild) addChild(component)
        return component
    }


    fun br(addAsChild: Boolean = true,init: (Component.() -> Unit) = {}): Component {
        val component = Component(type = "br", single = true)
        init.let { component.it() }
        if (addAsChild) addChild(component)
        return component
    }

    fun linebreak(addAsChild: Boolean = true,init: (Component.() -> Unit) = {}): Component {
        val component = Component(type = "br", single = true)
        init.let { component.it() }
        if (addAsChild) addChild(component)
        return component
    }

    fun script(addAsChild: Boolean = true,src: String? = null, content: String? = null, init: (Component.() -> Unit) = {}): Component {
        val component = Component(type = "script")
        init.let { component.it() }
        if (src != null) {
            component.attributes["src"] = src
        } else if (content != null) {
            component.innerHTML = content
        } else {
            return component
        }
        if (addAsChild) addChild(component)
        return component
    }

    fun isOfType(type: String): Boolean {
        return type == this.type
    }

    fun text(
        addAsChild: Boolean = true,
        type: TextType = TextType.TEXT,
        escapeHTML: Boolean = true,
        href: String? = null,
        text: String,
        init: (Component.() -> Unit) = {}
    ): Component {
        val component = Component(type = type.type)
        init.let { component.it() }
        if (type == TextType.LINK && href != null) {
            component.attributes["href"] = href
        }
        component.innerHTML = if (escapeHTML) {
            Util.ESCAPE_HTML4(text)
        } else {
            text
        }
        if (addAsChild) addChild(component)
        return component
    }


    fun input(
        addAsChild: Boolean = true,
        type: InputType,
        name: String,
        alt: String? = null,
        accept: String? = null,
        autoComplete: Boolean? = null,
        autoFocus: Boolean? = null,
        checked: Boolean? = null,
        dirName: String? = null,
        disabled: Boolean? = null,
        form: String? = null,
        formAction: URL? = null,
        formEncType: String? = null,
        formMethod: FormMethod? = null,
        formNoValidate: Boolean? = null,
        formTarget: String? = null,
        height: String? = null,
        list: String? = null,
        max: Int? = null,
        maxLength: Int? = null,
        min: Int? = null,
        minLength: Int? = null,
        multiple: Boolean? = null,
        pattern: Regex? = null,
        readonly: Boolean? = null,
        required: Boolean? = null,
        size: Int? = null,
        src: String? = null,
        step: Int? = null,
        value: String? = null,
        placeholder: String? = null,
        width: String? = null,
        init: (Component.() -> Unit) = {}
    ): Component {
        val component = createComponent(addAsChild,type = when (type) {
            InputType.TEXT_AREA -> "textarea"
            InputType.SELECT -> "select"
            InputType.OPTION -> "option"
            else -> "input"
        }, init = init)

        if (component.type == "input" && type.value != null) component.attributes["type"] = type.value
        component.attributes["name"] = name

        placeholder?.let { component.attributes["placeholder"] = it }
        value?.let { component.attributes["value"] = it }
        alt?.let { component.attributes["alt"] = it }
        accept?.let { component.attributes["accept"] = it }
        autoComplete?.let { component.attributes["autocomplete"] = it.toString() }
        autoFocus?.let { component.attributes["autofocus"] = it.toString() }
        checked?.let { component.attributes["checked"] = it.toString() }
        dirName?.let { component.attributes["dirName"] = it }
        disabled?.let { component.attributes["disabled"] = it.toString() }
        form?.let { component.attributes["form"] = it }
        formAction?.let { component.attributes["formaction"] = it.toString() }
        formEncType?.let { component.attributes["formenctype"] = it }
        formMethod?.let { component.attributes["formmethod"] = it.name.lowercase() }
        formNoValidate?.let { component.attributes["formnovalidate"] = it.toString() }
        formTarget?.let { component.attributes["formtarget"] = it }
        height?.let { component.attributes["height"] = it }
        list?.let { component.attributes["list"] = it }
        max?.let { component.attributes["max"] = it.toString() }
        maxLength?.let { component.attributes["maxlength"] = it.toString() }
        min?.let { component.attributes["min"] = it.toString() }
        minLength?.let { component.attributes["minlength"] = it.toString() }
        multiple?.let { component.attributes["multiple"] = "" }
        pattern?.let { component.attributes["pattern"] = it.pattern }
        readonly?.let { component.attributes["readonly"] = "" }
        required?.let { component.attributes["required"] = "" }
        size?.let { component.attributes["size"] = it.toString() }
        src?.let { component.attributes["src"] = it }
        step?.let { component.attributes["step"] = it.toString() }
        width?.let { component.attributes["width"] = it }
        return component
    }

    fun audio(
        addAsChild: Boolean = true,
        autoplay: Boolean? = null,
        controls: Boolean? = null,
        loop: Boolean? = null,
        muted: Boolean? = null,
        preload: String? = null,
        src: URL,
        init: (Component.() -> Unit) = {}
    ): Component {
        val component = createComponent(addAsChild,type = "audio", init = init)

        autoplay?.let { component.attributes["autoplay"] = "" }
        controls?.let { component.attributes["controls"] = "" }
        loop?.let { component.attributes["loop"] = "" }
        muted?.let { component.attributes["muted"] = "" }
        preload?.let { component.attributes["preload"] = it }
        src.let { component.attributes["src"] = it.toString() }

        return component
    }

    fun form(
        addAsChild: Boolean = true,action: URL, method: FormMethod, acceptCharset: String? = null, init: (Component.() -> Unit) = {}
    ): Component {
        val component = createComponent(addAsChild,type = "form", init  = init)
        component.attributes["action"] = action.toString()
        component.attributes["method"] = method.name.lowercase()
        acceptCharset?.let { component.attributes["accept-charset"] = it }
        return component
    }

    private fun createComponent(addAsChild: Boolean = true, type: String, init: Component.() -> Unit): Component {
        val component = Component(type = type)
        component.init()
        if (addAsChild) addChild(component)
        return component
    }

    fun article(addAsChild: Boolean = true,init: Component.() -> Unit): Component = createComponent(addAsChild,"article", init)
    fun aside(addAsChild: Boolean = true,init: Component.() -> Unit): Component = createComponent(addAsChild,"aside", init)

    fun option(addAsChild: Boolean = true,name: String, value: String, init: (Component.() -> Unit) = {}): Component {
        return input(addAsChild = addAsChild,type = InputType.OPTION, name = name, value = value, init = init)
    }

    fun area(
        addAsChild: Boolean = true,
        shape: String,
        coords: String,
        alt: String,
        href: URL,
        init: Component.() -> Unit
    ): Component = createComponent(addAsChild,"area") {
        init()
        attributes["shape"] = shape
        attributes["coords"] = coords
        attributes["alt"] = alt
        attributes["href"] = href.toString()
    }

    fun figure(addAsChild: Boolean = true,init: Component.() -> Unit): Component = createComponent(addAsChild,"figure", init)
    fun figureCaption(addAsChild: Boolean = true,text: String, init: Component.() -> Unit = {}): Component = createComponent(addAsChild,"figcaption") {
        innerHTML = text
        init()
    }

    fun image(
        addAsChild: Boolean = true,
        href: URL,
        alt: String,
        init: Component.() -> Unit = {}
    ): Component = createComponent(addAsChild,"img") {
        attributes["href"] = href.toString()
        attributes["alt"] = alt
        init()
    }

    fun video(
        addAsChild: Boolean = true,
        autoplay: Boolean? = null,
        controls: Boolean? = null,
        loop: Boolean? = null,
        muted: Boolean? = null,
        alt: String,
        src: URL,
        init: (Component.() -> Unit) = {}
    ): Component = createComponent(addAsChild,"video") {
        autoplay?.let { attributes["autoplay"] = ""}
        controls?.let { attributes["controls"] = ""}
        loop?.let { attributes["loop"] = ""}
        muted?.let { attributes["muted"] = ""}
        attributes["alt"] = alt
        attributes["src"] = src.toString()
        init()
    }

    fun link(addAsChild: Boolean = true,rel: String, href: URL, init: Component.() -> Unit = {}): Component = createComponent(addAsChild,"link") {
        attributes["rel"] = rel
        attributes["href"] = href.toString()
        init()
    }

    fun meta(addAsChild: Boolean = true,name: String, content: String, init: Component.() -> Unit = {}): Component = createComponent(addAsChild,"meta") {
        attributes["name"] = name
        attributes["content"] = content
        init()
    }

    fun listItem(addAsChild: Boolean = true,init: Component.() -> Unit = {}): Component = createComponent(addAsChild,"li", init)
    fun orderedList(addAsChild: Boolean = true,init: Component.() -> Unit = {}): Component = createComponent(addAsChild,"ol", init)
    fun unorderedList(addAsChild: Boolean = true,init: Component.() -> Unit = {}): Component = createComponent(addAsChild,"ul", init)


    fun nav(addAsChild: Boolean = true,init: Component.() -> Unit = {}): Component = createComponent(addAsChild,"nav", init)

    fun button(addAsChild: Boolean = true,text: String, init: (Component.() -> Unit) = {}): Component {
        val component = Component(type = "button", innerHTML = text)
        component.init()
        if (addAsChild) addChild(component)
        return component
    }

    fun caption(addAsChild: Boolean = true,content: String, init: Component.() -> Unit = {}): Component = createComponent(addAsChild,"caption") {
        this.innerHTML = content
        init()
    }

    fun table(addAsChild: Boolean = true,init: Component.() -> Unit): Component = createComponent(addAsChild,"table", init)
    fun tableData(addAsChild: Boolean = true,data: String, init: Component.() -> Unit = {}): Component = createComponent(addAsChild,"td") {
        innerHTML = data
        init()
    }
    fun tableHead(addAsChild: Boolean = true,init: Component.() -> Unit = {}): Component = createComponent(addAsChild,"thead") {
        init()
    }
    fun tableBody(addAsChild: Boolean = true,init: Component.() -> Unit = {}): Component = createComponent(addAsChild,"tbody") {
        init()
    }
    fun tableRow(addAsChild: Boolean = true,init: Component.() -> Unit): Component = createComponent(addAsChild,"tr", init)

    fun on(trigger: Trigger, init: (Action.(Component) -> Unit) = {}) {
        val action = Action(Action.Type.NONE, this, trigger)
        action.init(this)
        actions.add(action)
    }

    fun addStylings(vararg styles: Styling) {
        styles.forEach { stylingToAdd ->
            if (this.styles.any { it.selector == stylingToAdd.selector }) {
                this.styles.filter { it.selector == stylingToAdd.selector }.forEach { style ->
                    stylingToAdd.styles.forEach {
                        style.append(it)
                    }
                }
            } else {
                this.styles.add(stylingToAdd)
            }
        }
    }

    fun style(selector: String = this.id, styles: Map<String, Any> = mapOf(), init: Styling.() -> Unit = {}) {
        val styling = Styling(selector)
        styles.forEach { (name, value) -> styling.add(name, value) }
        styling.init()
        addStylings(styling)
    }
}



