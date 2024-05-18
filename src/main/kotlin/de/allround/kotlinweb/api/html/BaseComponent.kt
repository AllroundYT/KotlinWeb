package de.allround.kotlinweb.api.html

import java.util.*

class BaseComponent(
    id: String = "ID${UUID.randomUUID().toString().replace("-", "")}",
    classes: MutableSet<String> = mutableSetOf(),
    children: MutableList<Component<*>> = mutableListOf(),
    type: String,
    single: Boolean = false,
    attributes: MutableMap<String, String> = mutableMapOf(),
    innerHTML: Any? = null,
    renderAsChildren: Boolean = true,
    componentInit: BaseComponent.() -> Unit = {}
) : Component<BaseComponent>(
    id, classes, children, type, single, attributes, innerHTML, renderAsChildren, componentInit
)