package de.allround.kotlinweb.api.styles

import de.allround.kotlinweb.api.components.Component
import io.vertx.ext.web.Session

object GeneratedStylesheet {
    fun get(session: Session): Stylesheet {
        session.putIfAbsent("generated-styles", Stylesheet())
        return session.get("generated-styles")
    }

    fun append(session: Session, component: Component): GeneratedStylesheet {
        val stylesheet = get(session)
        stylesheet.append(component)
        session.put("generated-styles", stylesheet)
        return this
    }

    fun append(session: Session, stylesheet: Stylesheet): GeneratedStylesheet {
        val generatedStylesheet = get(session)
        generatedStylesheet.append(stylesheet)
        session.put("generated-styles", generatedStylesheet)
        return this
    }

    fun set(session: Session, component: Component): GeneratedStylesheet {
        reset(session)
        val stylesheet = get(session)
        stylesheet.append(component)
        session.put("generated-styles", stylesheet)
        return this
    }

    fun set(session: Session, stylesheet: Stylesheet): GeneratedStylesheet {
        reset(session)
        val generatedStylesheet = get(session)
        generatedStylesheet.append(stylesheet)
        session.put("generated-styles", generatedStylesheet)
        return this
    }

    fun asComponent(session: Session): Component {
        return Component(type = "style", id = "generated-styles", innerHTML = get(session).toString()) {
            attributes["hx-swap-oob"] = "true"
        }
    }

    fun reset(session: Session) {
        session.put("generated-styles", Stylesheet())
    }
}