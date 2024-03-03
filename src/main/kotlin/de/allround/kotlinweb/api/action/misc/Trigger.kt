package de.allround.kotlinweb.api.action.misc

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





enum class DomEvents {
    CLICK,
    ABORT,
    AFTERPRINT,
    ANIMATIONEND,
    ANIMATIONITERATION,
    ANIMATIONSTART,
    BEFOREPRINT,
    BEFOREUNLOAD,
    BLUR,
    CANPLAY,
    CANPLAYTHROUGH,
    CHANGE,
    CONTEXTMENU,
    COPY,
    CUT,
    DBLCLICK,
    DRAG,
    DRAGEND,
    DRAGENTER,
    DRAGLEAVE,
    DRAGOVER,
    DRAGSTART,
    DROP,
    DURATIONCHANGE,
    ENDED,
    ERROR,
    FOCUS,
    FOCUSIN,
    FOCUSOUT,
    FULLSCREENCHANGE,
    FULLSCREENERROR,
    HASHCHANGE,
    INPUT,
    INVALID,
    KEYDOWN,
    KEYPRESS,
    KEYUP,
    LOAD,
    LOADEDDATA,
    LOADEDMETADATA,
    LOADSTART,
    MESSAGE,
    MOUSEDOWN,
    MOUSEENTER,
    MOUSELEAVE,
    MOUSEMOVE,
    MOUSEOUT,
    MOUSEOVER,
    MOUSEUP,
    MOUSEWHEEL,
    OFFLINE,
    ONLINE,
    OPEN,
    PAGEHIDE,
    PAGESHOW,
    PASTE,
    PAUSE,
    PLAY,
    PLAYING,
    POPSTATE,
    PROGRESS,
    RATECHANGE,
    RESIZE,
    RESET,
    SCROLL,
    SEARCH,
    SEEKED,
    SEEKING,
    SELECT,
    SHOW,
    STALLED,
    STORAGE,
    SUBMIT,
    SUSPEND,
    TIMEUPDATE,
    TOGGLE,
    TOUCHCANCEL,
    TOUCHEND,
    TOUCHMOVE,
    TOUCHSTART,
    TRANSITIONEND,
    UNLOAD,
    VOLUMECHANGE,
    WAITING,
    WHEEL
}