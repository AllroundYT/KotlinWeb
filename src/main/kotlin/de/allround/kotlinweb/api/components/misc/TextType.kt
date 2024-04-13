package de.allround.kotlinweb.api.components.misc

enum class TextType(val type: String) {
    H1("h1"),
    H2("h2"),
    H3("h3"),
    H4("h4"),
    H5("h5"),
    H6("h6"),
    TEXT("p"),
    LINK("a"),
    SPAN("span"),
    CODE("code"),
    ITALIC("i"),
    STRIKETHROUGH("s"),
    UNDERLINED("u"),
    BOLD("b")
}