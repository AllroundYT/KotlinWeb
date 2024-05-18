package de.allround.kotlinweb.api.action.hyperscript

import de.allround.kotlinweb.api.action.Action
import de.allround.kotlinweb.api.action.trigger.Trigger
import de.allround.kotlinweb.api.html.Component
import kotlin.time.Duration

class WaitAction(val duration: Duration, component: Component<*>, trigger: Trigger): Action(Type.HYPERSCRIPT, component,
    trigger
) {
    override fun toString(): String {
        return "wait ${duration.inWholeMilliseconds}ms "
    }
}