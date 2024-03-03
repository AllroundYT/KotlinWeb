package de.allround.kotlinweb.api.action

import de.allround.kotlinweb.api.action.misc.Trigger
import de.allround.kotlinweb.api.components.Component

class HtmxAction(trigger: Trigger, val init: HtmxAction.(Component) -> Unit = {}) : Action(trigger) {
    private var apply: MutableList<(Component.() -> Unit)> = mutableListOf()

    override fun apply(component: Component) {
        init(component)
        apply.forEach { component.it() }
    }


}