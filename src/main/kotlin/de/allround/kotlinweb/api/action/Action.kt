package de.allround.kotlinweb.api.action

import de.allround.kotlinweb.api.action.misc.Trigger
import de.allround.kotlinweb.api.components.Component

abstract class Action(val trigger: Trigger) {
    abstract fun apply(component: Component)
}