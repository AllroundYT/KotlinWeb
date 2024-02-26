package de.allround.kotlinweb.api.components

import de.allround.kotlinweb.de.allround.kotlinweb.WebApplication
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.Session
import de.allround.kotlinweb.api.components.misc.HtmxAttributes
import de.allround.kotlinweb.api.components.misc.Stylesheet
import de.allround.kotlinweb.api.components.misc.Styling
import de.allround.kotlinweb.util.MultiMap
import de.allround.kotlinweb.util.Util.ESCAPE_HTML4
import java.util.*

open class Component(
    val type: String,
    val single: Boolean = false,
    var id: String? = null,
    val classes: MutableList<String> = mutableListOf(),
    val styles: MultiMap<String, Any> = MultiMap(),
    val children: MutableList<Component> = mutableListOf(),
    val attributes: MultiMap<String, Any> = MultiMap(),
    var content: Any? = null,
    var htmxAttributes: HtmxAttributes? = null,
    val stylesheet: Stylesheet? = null,
    var styling: Styling? = null //TODO: replace with styling list
) {

    open fun copy(): Component {
        return Component(
            type, single, id, classes, styles,children, attributes, content, htmxAttributes,stylesheet, styling
        )
    }

    open fun init(routingContext: RoutingContext): Component {
        return this
    }

    fun render(session: Session, webApplication: WebApplication, includeStyles: Boolean = false): String {
        htmxAttributes?.apply(this)
        if (classes.isNotEmpty()) attributes.add("class", *classes.toTypedArray())


        //add open tag
        val htmlBuilder = StringBuilder()

        if (includeStyles) {
            styling?.let {
                htmlBuilder.append(Component(type = "style", content = it.generateStylesheet(this)).render(session, webApplication))
            }
            stylesheet?.let {
                htmlBuilder.append(Component(type = "style", content = it.generateCss()).render(session, webApplication))
            }
            styles.map.forEach { (key, valueList) ->
                val builder = StringBuilder()
                valueList.forEach {
                    if (builder.isNotEmpty()) builder.append(" ")
                    builder.append(it.toString())
                }
                attributes.add("style", "$key:$builder;")
            }
        }
        htmlBuilder.append("<").append(type)
        if (id != null) htmlBuilder.append(" id=").append("\"").append(id).append("\"")
        attributes.forEach { attributeName, values ->
            if (attributeName.equals("hx-ext", ignoreCase = true)) {
                htmlBuilder.append(" ")
                    .append(attributeName)
                    .append("=")
                    .append("\"")
                    .append(values.joinToString(", "))
                    .append("\"")
            } else {
                htmlBuilder.append(" ")
                    .append(attributeName)
                    .append("=")
                    .append("\"")
                    .append(values.joinToString(" "))
                    .append("\"")
            }
        }
        if (single) {
            //close and return single tags
            htmlBuilder.append("/>")
            return htmlBuilder.toString()
        }
        htmlBuilder.append(">")

        //add content between tags
        if (content != null) htmlBuilder.append(ESCAPE_HTML4(content.toString()))
        if (children.isNotEmpty()) htmlBuilder.append(System.lineSeparator())
        children.forEach {
            htmlBuilder.append(it.render(session,webApplication,includeStyles))
        }

        //add close tag
        htmlBuilder.append("</").append(type).append(">")
        return htmlBuilder.toString()
    }

    fun generateStylesheet(): String {

        stylesheet?.let {
            if (styling == null) {
                styling = Styling(stylesheet = stylesheet)
            } else{
                styling?.stylesheet = it
            }
        }

        val builder = StringBuilder()

        children.forEach {
            builder.append(it.generateStylesheet())
        }

        if (styles.isEmpty && styling == null) return builder.toString()

        if (id == null) id = UUID.randomUUID().toString().lowercase().replace("-", "")
        styling?.let { builder.append(it.generateStylesheet()) }
        val componentStyleBuilder = StringBuilder()
        styles.map.forEach { (key, values) ->
            val valueBuilder = StringBuilder()
            values.forEach {
                if (valueBuilder.isNotEmpty()) valueBuilder.append(" ")
                valueBuilder.append(it)
            }
            componentStyleBuilder.append("$key: $valueBuilder;")
        }
        builder.append("#$id { $componentStyleBuilder }")

        return builder.toString()
    }
}

