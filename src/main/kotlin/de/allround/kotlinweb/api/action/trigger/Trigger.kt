package de.allround.kotlinweb.api.action.trigger

sealed interface Trigger {
    fun asScriptTrigger(): String
    fun asHtmxTrigger(): String

    data class DomEvent(val type: DomEvents, val data: String? = null) : Trigger {
        override fun asScriptTrigger(): String = "on ${type.name.lowercase()} ${data?.plus(" ") ?: ""}"
        override fun asHtmxTrigger(): String = "${type.name.lowercase()} ${data ?: ""}"
    }


    data class Custom(val trigger: String) : Trigger {
        override fun asScriptTrigger(): String  = "on $trigger "
        override fun asHtmxTrigger(): String = trigger
    }
}





