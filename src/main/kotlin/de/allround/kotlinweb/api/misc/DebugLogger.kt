package de.allround.kotlinweb.api.misc

object DebugLogger {
    var isEnabled = false

    fun info(tag: String, message: String) {
        if (isEnabled) {
            println("[$tag - Info] $message")
        }
    }

    fun exception(tag: String, message: String) {
        if (isEnabled) {
            System.err.println("[$tag - Exception] $message")
        }
    }

    fun exception(tag: String, throwable: Throwable, message: String) {
        if (isEnabled) {
            System.err.println("[$tag - Exception] $message")
            throwable.printStackTrace(System.err)
        }
    }
}