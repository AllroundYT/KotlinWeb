package de.allround.kotlinweb.api.components.misc

enum class InputType(val value: String? = null) {
    TEXT("text"),
    TEXT_AREA,
    PASSWORD("password"),
    EMAIL("email"),
    NUMBER("number"),
    TEL("tel"),
    URL("url"),
    DATE("date"),
    TIME("time"),
    DATETIME_LOCAL("datetime-local"),
    MONTH("month"),
    WEEK("week"),
    CHECKBOX("checkbox"),
    RADIO("radio"),
    FILE("file"),
    SELECT,
    OPTION
}