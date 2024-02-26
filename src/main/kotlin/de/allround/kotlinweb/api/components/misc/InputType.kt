package de.allround.kotlinweb.api.components.misc

enum class InputType() {
    BUTTON(),
    CHECKBOX,
    COLOR,
    DATE,
    DATE_TIME_LOCAL,
    EMAIL,
    FILE,
    HIDDEN,
    IMAGE,
    MONTH,
    NUMBER,
    PASSWORD,
    RADIO_BUTTON,
    RANGE,
    RESET_BUTTON,
    SEARCH,
    SUBMIT_BUTTON,
    TELEPHONE,
    TEXT,
    TIME,
    URL,
    WEEK;

    override fun toString(): String {
        return name.lowercase().replace("_","")
    }
}