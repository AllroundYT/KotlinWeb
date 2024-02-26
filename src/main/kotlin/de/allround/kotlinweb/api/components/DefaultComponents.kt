package de.allround.kotlinweb.api.components

import de.allround.kotlinweb.api.components.misc.*
import de.allround.kotlinweb.util.MultiMap


//Head tags
class Head(val title: String) : Component(
    type = "head",
    single = false,
    id = null,
    content = null,
    children = mutableListOf(),
    attributes = MultiMap(),
    classes = mutableListOf(),
    htmxAttributes = null,
    styling = null
) {
    init {
        attributes.add("title", title)
    }
}

class Script(
    id: String? = null,
    classes: MutableList<String> = mutableListOf(),
    children: MutableList<Component> = mutableListOf(),
    attributes: MultiMap<String, Any> = MultiMap(),
    content: Any? = null,
    src: String
) : Component(
    type = "script",
    single = false,
    id = id,
    content = content,
    children = children,
    attributes = attributes,
    classes = classes,
    htmxAttributes = null,
    styling = null
) {
    init {
        this.attributes.add("src", src)
    }
}

class HeadLink(
    rel: String,
    href: String,
    id: String? = null,
    classes: MutableList<String> = mutableListOf(),
    children: MutableList<Component> = mutableListOf(),
    attributes: MultiMap<String, Any> = MultiMap(),
    content: Any? = null
) : Component(
    type = "link",
    single = false,
    id = id,
    content = content,
    children = children,
    attributes = attributes,
    classes = classes,
    htmxAttributes = null,
    styling = null
) {
    init {
        this.attributes.add("rel", rel)
        this.attributes.add("href", href)
    }
}

class Meta(
    name: String,
    content: String,
    id: String? = null,
    classes: MutableList<String> = mutableListOf(),
    children: MutableList<Component> = mutableListOf(),
    attributes: MultiMap<String, Any> = MultiMap()
) : Component(
    type = "REPLACE",
    single = false,
    id = id,
    content = null,
    children = children,
    attributes = attributes,
    classes = classes,
    htmxAttributes = null,
    styling = null
) {
    init {
        this.attributes.add("name", name)
        this.attributes.add("content", content)
    }
}


//Body tags

class Body(htmxAttributes: HtmxAttributes? = null) : Component(
    type = "body",
    single = false,
    id = null,
    content = null,
    children = mutableListOf(),
    attributes = MultiMap(),
    classes = mutableListOf(),
    htmxAttributes = htmxAttributes,
    styling = null
)

//Container tags
open class Div(
    id: String? = null,
    classes: MutableList<String> = mutableListOf(),
    children: MutableList<Component> = mutableListOf(),
    attributes: MultiMap<String, Any> = MultiMap(),
    content: Any? = null,
    htmxAttributes: HtmxAttributes? = null,
    styling: Styling? = null
) : Component(
    content = content,
    id = id,
    type = "div",
    classes = classes,
    children = children,
    attributes = attributes,
    single = false,
    htmxAttributes = htmxAttributes,
    styling = styling
)

//form
class Form(
    action: String,
    method: String,
    id: String? = null,
    classes: MutableList<String> = mutableListOf(),
    children: MutableList<Component> = mutableListOf(),
    attributes: MultiMap<String, Any> = MultiMap(),
    content: Any? = null,
    htmxAttributes: HtmxAttributes? = null,
    styling: Styling? = null
) : Component(
    type = "form",
    single = false,
    id = id,
    content = content,
    children = children,
    attributes = attributes,
    classes = classes,
    htmxAttributes = htmxAttributes,
    styling = styling
) {
    init {
        this.attributes.add("action", action)
        this.attributes.add("method", method)
    }
}

class Input(
    type: InputType,
    inputAttributes: InputAttributes? = null,
    classes: MutableList<String> = mutableListOf(),
    children: MutableList<Component> = mutableListOf(),
    attributes: MultiMap<String, Any> = MultiMap(),
    content: Any? = null,
    htmxAttributes: HtmxAttributes? = null,
    id: String? = null,
    styling: Styling? = null
) : Component(
    type = "input",
    single = false,
    id = id,
    content = content,
    children = children,
    attributes = attributes,
    classes = classes,
    htmxAttributes = htmxAttributes,
    styling = styling
) {
    init {
        type.let { this.attributes.add("type", it) }
        inputAttributes?.apply(this)
    }
}

class Button(
    id: String? = null,
    classes: MutableList<String> = mutableListOf(),
    children: MutableList<Component> = mutableListOf(),
    attributes: MultiMap<String, Any> = MultiMap(),
    content: String,
    htmxAttributes: HtmxAttributes,
    styling: Styling? = null
) : Component(
    type = "button",
    single = false,
    id = id,
    content = content,
    children = children,
    attributes = attributes,
    classes = classes,
    htmxAttributes = htmxAttributes,
    styling = styling
)

class Media(
    type: MediaType,
    mediaAttributes: MediaAttributes,
    id: String? = null,
    classes: MutableList<String> = mutableListOf(),
    children: MutableList<Component> = mutableListOf(),
    attributes: MultiMap<String, Any> = MultiMap(),
    content: Any? = null,
    htmxAttributes: HtmxAttributes? = null,
    styling: Styling? = null
) : Component(
    type = type.toString(),
    single = false,
    id = id,
    content = content,
    children = children,
    attributes = attributes,
    classes = classes,
    htmxAttributes = htmxAttributes,
    styling = styling
) {
    init {
        mediaAttributes.apply(this)
    }
}

class Link(
    href: String,
    id: String? = null,
    classes: MutableList<String> = mutableListOf(),
    children: MutableList<Component> = mutableListOf(),
    attributes: MultiMap<String, Any> = MultiMap(),
    content: Any? = null,
    htmxAttributes: HtmxAttributes? = null,
    styling: Styling? = null
) : Component(
    type = "a",
    single = false,
    id = id,
    content = content,
    children = children,
    attributes = attributes,
    classes = classes,
    htmxAttributes = htmxAttributes,
    styling = styling
) {
    init {
        this.attributes.add("href", href)
    }
}

class ListItem(
    id: String? = null,
    classes: MutableList<String> = mutableListOf(),
    children: MutableList<Component> = mutableListOf(),
    attributes: MultiMap<String, Any> = MultiMap(),
    content: Any? = null,
    htmxAttributes: HtmxAttributes? = null,
    styling: Styling? = null
) : Component(
    type = "li",
    single = false,
    id = id,
    content = content,
    children = children,
    attributes = attributes,
    classes = classes,
    htmxAttributes = htmxAttributes,
    styling = styling
)

class OrderedList(
    id: String? = null,
    classes: MutableList<String> = mutableListOf(),
    children: MutableList<out ListItem> = mutableListOf(),
    attributes: MultiMap<String, Any> = MultiMap(),
    content: Any? = null,
    htmxAttributes: HtmxAttributes? = null,
    styling: Styling? = null
) : Component(
    type = "ol",
    single = false,
    id = id,
    content = content,
    children = children.toMutableList(),
    attributes = attributes,
    classes = classes,
    htmxAttributes = htmxAttributes,
    styling = styling
)

class UnorderedList(
    id: String? = null,
    classes: MutableList<String> = mutableListOf(),
    children: MutableList<out ListItem> = mutableListOf(),
    attributes: MultiMap<String, Any> = MultiMap(),
    content: Any? = null,
    htmxAttributes: HtmxAttributes? = null,
    styling: Styling? = null
) : Component(
    type = "ul",
    single = false,
    id = id,
    content = content,
    children = children.toMutableList(),
    attributes = attributes,
    classes = classes,
    htmxAttributes = htmxAttributes,
    styling = styling
)


class Table(
    id: String? = null,
    classes: MutableList<String> = mutableListOf(),
    children: MutableList<Component> = mutableListOf(),
    attributes: MultiMap<String, Any> = MultiMap(),
    content: Any? = null,
    htmxAttributes: HtmxAttributes? = null,
    styling: Styling? = null
) : Component(
    type = "table",
    single = false,
    id = id,
    content = content,
    children = children,
    attributes = attributes,
    classes = classes,
    htmxAttributes = htmxAttributes,
    styling = styling
)

interface TR
class TableData(
    id: String? = null,
    classes: MutableList<String> = mutableListOf(),
    children: MutableList<Component> = mutableListOf(),
    attributes: MultiMap<String, Any> = MultiMap(),
    content: Any? = null,
    htmxAttributes: HtmxAttributes? = null,
    styling: Styling? = null
) : Component(
    type = "td",
    single = content == null,
    id = id,
    content = content,
    children = children,
    attributes = attributes,
    classes = classes,
    htmxAttributes = htmxAttributes,
    styling = styling
), TR

class TableHead(
    id: String? = null,
    classes: MutableList<String> = mutableListOf(),
    children: MutableList<Component> = mutableListOf(),
    attributes: MultiMap<String, Any> = MultiMap(),
    content: Any? = null,
    htmxAttributes: HtmxAttributes? = null,
    styling: Styling? = null
) : Component(
    type = "th",
    single = content == null,
    id = id,
    content = content,
    children = children,
    attributes = attributes,
    classes = classes,
    htmxAttributes = htmxAttributes,
    styling = styling
), TR

class TemplateRow<T> (
    type: String = "tr",
    id: String? = null,
    single: Boolean = false,
    classes: MutableList<String> = mutableListOf(),
    children: MutableList<T> = mutableListOf() ,
    attributes: MultiMap<String, Any> = MultiMap(),
    content: Any? = null,
    htmxAttributes: HtmxAttributes? = null,
    styling: Styling? = null
)  : Component(
    type = type,
    single = single,
    id = id,
    content = content,
    children = children.toMutableList(),
    attributes = attributes,
    classes = classes,
    htmxAttributes = htmxAttributes,
    styling = styling
) where T : Component, T : TR
