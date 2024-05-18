package de.allround.kotlinweb.api.html

abstract class HtmlFragment : Component<HtmlFragment>(type = "") {
    abstract override fun init()

    override fun toString(): String {
        init()
        val builder = StringBuilder()
        this.children.forEach {
            if (it.renderAsChildren) {
                builder.append(it.toString()).append("\n")
            }
        }
        return builder.toString()
    }
}