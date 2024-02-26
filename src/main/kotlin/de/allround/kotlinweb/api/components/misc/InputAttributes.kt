package de.allround.kotlinweb.api.components.misc

import de.allround.kotlinweb.api.components.Component
import java.net.URL
import java.util.regex.Pattern

data class InputAttributes(
    val accept: String? = null,
    val alt: String? = null,
    val autoComplete: Boolean? = null,
    val autoFocus: Boolean? = null,
    val checked: Boolean? = null,
    val dirname: String? = null,
    val disabled: Boolean? = null,
    val form: String? = null,
    val formaction: URL? = null,
    val formenctype: String? = null,
    val formmethod: FormMethods? = null,
    val formnovalidate: Boolean? = null,
    val formtarget: String? = null,
    val height: Int? = null,
    val list: String? = null,
    val max: Int? = null,
    val maxlength: Int? = null,
    val min: Int? = null,
    val minlength: Int? = null,
    val multiple: Boolean? = null,
    val name: String? = null,
    val pattern: Pattern? = null,
    val placeholder: String? = null,
    val readonly: Boolean? = null,
    val required: Boolean? = null,
    val size: Int? = null,
    val src: URL? = null,
    val step: Int? = null,
    val value: String? = null,
    val width: Int? = null
) {
    fun apply(component: Component) {
        accept?.let { component.attributes.add("accept", it) }
        alt?.let { component.attributes.add("alt", it) }
        autoComplete?.let { component.attributes.add("autocomplete", it) }
        autoFocus?.let { component.attributes.add("autofocus", it) }
        checked?.let { component.attributes.add("checked", it) }
        dirname?.let { component.attributes.add("dirname", it) }
        disabled?.let { component.attributes.add("disabled", it) }
        form?.let { component.attributes.add("form", it) }
        formaction?.let { component.attributes.add("formaction", it.toString()) }
        formenctype?.let { component.attributes.add("formenctype", it) }
        formmethod?.let { component.attributes.add("formmethod", it) }
        formnovalidate?.let { component.attributes.add("formnovalidate", it) }
        formtarget?.let { component.attributes.add("formtarget", it) }
        height?.let { component.attributes.add("height", it) }
        list?.let { component.attributes.add("list", it) }
        max?.let { component.attributes.add("max", it) }
        maxlength?.let { component.attributes.add("maxlength", it) }
        min?.let { component.attributes.add("min", it) }
        minlength?.let { component.attributes.add("minlength", it) }
        multiple?.let { component.attributes.add("multiple", it) }
        name?.let { component.attributes.add("name", it) }
        pattern?.let { component.attributes.add("pattern", it.pattern()) }
        placeholder?.let { component.attributes.add("placeholder", it) }
        readonly?.let { component.attributes.add("readonly", it) }
        required?.let { component.attributes.add("required", it) }
        size?.let { component.attributes.add("size", it) }
        src?.let { component.attributes.add("src", it.toString()) }
        step?.let { component.attributes.add("step", it) }
        value?.let { component.attributes.add("value", it) }
        width?.let { component.attributes.add("width", it) }
    }
}
