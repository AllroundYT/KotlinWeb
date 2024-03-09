package de.allround.kotlinweb.api.styles

import java.awt.Color

class Styling(
    var selector: String, val styles: MutableList<Style> = mutableListOf()
) {
    fun append(style: Style) {
        styles.removeIf { it.name == style.name }
        styles.add(style)
    }

    override fun toString(): String {
        val builder = StringBuilder()

        builder.append(selector)

        builder.append(" {").append(System.lineSeparator())
        styles.forEach {
            builder.append(it.toString()).append(System.lineSeparator())
        }
        builder.append(" }")

        return builder.toString()
    }

    fun add(name: String, value: Any): Style {
        styles.removeIf { it.name == name }
        val style = Style(name, value)
        append(style)
        return style
    }

    fun fontStyle(style: String): Style = add("font-style", style)
    fun fontVariant(variant: String): Style = add("font-variant", variant)
    fun fontWeight(weight: String): Style = add("font-weight", weight)
    fun fontSize(size: String): Style = add("font-size", size)
    fun fontFamily(family: String): Style = add("font-family", family)
    fun textAlign(align: String): Style = add("text-align", align)
    fun letterSpacing(space: String): Style = add("letter-spacing", space)
    fun textOutline(outline: String): Style = add("text-outline", outline)
    fun wordWrap(wrap: String): Style = add("word-wrap", wrap)
    fun textIndent(indent: String): Style = add("text-indent", indent)
    fun wordSpacing(spacing: String): Style = add("word-spacing", spacing)
    fun textTransform(transform: String): Style = add("text-transform", transform)
    fun textEmphasis(emphasis: String): Style = add("text-emphasis", emphasis)
    fun textJustify(justify: String): Style = add("text-justify", justify)
    fun cursor(cursor: String): Style = add("cursor", cursor)
    fun navIndex(index: String): Style = add("nav-index", index)
    fun navUp(up: String): Style = add("nav-up", up)
    fun navDown(down: String): Style = add("nav-down", down)
    fun navLeft(left: String): Style = add("nav-left", left)
    fun navRight(right: String): Style = add("nav-right", right)
    fun resize(resize: String): Style = add("resize", resize)
    fun icon(icon: String): Style = add("icon", icon)
    fun backgroundSize(size: String): Style = add("background-size", size)
    fun backgroundImage(image: String): Style = add("background-image", image)
    fun backgroundRepeat(repeat: String): Style = add("background-repeat", repeat)
    fun backgroundAttachment(attachment: String): Style = add("background-attachment", attachment)
    fun backgroundColor(color: String): Style = add("background-color", color)
    fun backgroundPosition(position: String): Style = add("background-position", position)
    fun backgroundOrigin(origin: String): Style = add("background-origin", origin)
    fun backgroundClip(clip: String): Style = add("background-clip", clip)
    fun borderWidth(width: String): Style = add("border-width", width)
    fun borderStyle(style: String): Style = add("border-style", style)
    fun borderColor(color: String): Style = add("border-color", color)
    fun borderLeft(left: String): Style = add("border-left", left)
    fun borderRight(right: String): Style = add("border-right", right)
    fun borderTop(top: String): Style = add("border-top", top)
    fun borderBottom(bottom: String): Style = add("border-bottom", bottom)
    fun borderDecorationBreak(breakStyle: String): Style = add("border-decoration-break", breakStyle)
    fun borderRadius(radius: String): Style = add("border-radius", radius)
    fun borderImage(image: String): Style = add("border-image", image)
    fun height(height: String): Style = add("height", height)
    fun maxHeight(height: String): Style = add("max-height", height)
    fun maxWidth(width: String): Style = add("max-width", width)
    fun minHeight(height: String): Style = add("min-height", height)
    fun minWidth(width: String): Style = add("min-width", width)
    fun marginBottom(margin: String): Style = add("margin-bottom", margin)
    fun marginLeft(margin: String): Style = add("margin-left", margin)
    fun marginRight(margin: String): Style = add("margin-right", margin)
    fun marginTop(margin: String): Style = add("margin-top", margin)
    fun paddingBottom(padding: String): Style = add("padding-bottom", padding)
    fun paddingTop(padding: String): Style = add("padding-top", padding)
    fun paddingRight(padding: String): Style = add("padding-right", padding)
    fun paddingLeft(padding: String): Style = add("padding-left", padding)
    fun display(display: String): Style = add("display", display)
    fun marqueeDirection(direction: String): Style = add("marquee-direction", direction)
    fun marqueeLoop(loop: String): Style = add("marquee-loop", loop)
    fun marqueePlayCount(count: String): Style = add("marquee-play-count", count)
    fun marqueeSpeed(speed: String): Style = add("marquee-speed", speed)
    fun marqueeStyle(style: String): Style = add("marquee-style", style)
    fun overflow(overflow: String): Style = add("overflow", overflow)
    fun overflowStyle(style: String): Style = add("overflow-style", style)
    fun overflowX(overflow: String): Style = add("overflow-x", overflow)
    fun rotation(rotation: String): Style = add("rotation", rotation)
    fun rotationPoint(point: String): Style = add("rotation-point", point)
    fun visibility(visibility: String): Style = add("visibility", visibility)
    fun clear(clear: String): Style = add("clear", clear)

    // Template layout properties
    fun boxAlign(align: String): Style = add("box-align", align)
    fun boxDirection(direction: String): Style = add("box-direction", direction)
    fun boxFlex(flex: String): Style = add("box-flex", flex)
    fun boxFlexGroup(group: Int): Style = add("box-flex-group", group)
    fun boxLines(lines: String): Style = add("box-lines", lines)
    fun boxOrient(orient: String): Style = add("box-orient", orient)
    fun boxPack(pack: String): Style = add("box-pack", pack)
    fun boxSizing(sizing: String): Style = add("box-sizing", sizing)
    fun tabSide(side: String): Style = add("tab-side", side)

    // Table properties
    fun borderCollapse(collapse: String): Style = add("border-collapse", collapse)
    fun emptyCells(cells: String): Style = add("empty-cells", cells)
    fun borderSpacing(spacing: String): Style = add("border-spacing", spacing)
    fun tableLayout(layout: String): Style = add("table-layout", layout)
    fun captionSide(side: String): Style = add("caption-side", side)

    // Columns properties
    fun columnCount(count: String): Style = add("column-count", count)
    fun columnFill(fill: String): Style = add("column-fill", fill)
    fun columnGap(gap: String): Style = add("column-gap", gap)
    fun columnRuleWidth(width: String): Style = add("column-rule-width", width)
    fun columnRuleStyle(style: String): Style = add("column-rule-style", style)
    fun columnRuleColor(color: String): Style = add("column-rule-color", color)
    fun columnWidth(width: String): Style = add("column-width", width)
    fun columnSpan(span: String): Style = add("column-span", span)

    // Colors properties
    fun color(color: String): Style = add("color", color)
    fun opacity(opacity: String): Style = add("opacity", opacity)

    // Grid positioning properties
    fun gridColumns(columns: String): Style = add("grid-columns", columns)
    fun gridRows(rows: String): Style = add("grid-rows", rows)

    // List and Markers properties
    fun listStyleType(type: String): Style = add("list-style-type", type)
    fun listStylePosition(position: String): Style = add("list-style-position", position)
    fun listStyleImage(image: String): Style = add("list-style-image", image)
    fun markerOffset(offset: String): Style = add("marker-offset", offset)

    // Animations properties
    fun animationName(name: String): Style = add("animation-name", name)
    fun animationDuration(duration: String): Style = add("animation-duration", duration)
    fun animationTimingFunction(function: String): Style = add("animation-timing-function", function)
    fun animationDelay(delay: String): Style = add("animation-delay", delay)
    fun animationIterationCount(count: String): Style = add("animation-iteration-count", count)
    fun animationDirection(direction: String): Style = add("animation-direction", direction)
    fun animationPlayState(state: String): Style = add("animation-play-state", state)
    fun animationFillMode(mode: String): Style = add("animation-fill-mode", mode)

    // Outline properties
    fun outlineColor(color: String): Style = add("outline-color", color)
    fun outlineStyle(style: String): Style = add("outline-style", style)
    fun outlineWidth(width: String): Style = add("outline-width", width)
    fun outlineOffset(offset: String): Style = add("outline-offset", offset)

    // Hyperlink properties
    fun targetName(name: String): Style = add("target-name", name)
    fun targetNew(new: String): Style = add("target-new", new)
    fun targetPosition(position: String): Style = add("target-position", position)

    // Paged Media properties
    fun fit(fit: String): Style = add("fit", fit)
    fun fitPosition(position: String): Style = add("fit-position", position)
    fun orphans(orphans: String): Style = add("orphans", orphans)
    fun imageOrientation(orientation: String): Style = add("image-orientation", orientation)
    fun page(page: String): Style = add("page", page)
    fun pageBreakAfter(breakAfter: String): Style = add("page-break-after", breakAfter)
    fun pageBreakBefore(breakBefore: String): Style = add("page-break-before", breakBefore)
    fun pageBreakInside(breakInside: String): Style = add("page-break-inside", breakInside)

    // Positioning properties
    fun bottom(bottom: String): Style = add("bottom", bottom)
    fun right(right: String): Style = add("right", right)
    fun top(top: String): Style = add("top", top)
    fun left(left: String): Style = add("left", left)
    fun zIndex(zIndex: String): Style = add("z-index", zIndex)
    fun clip(clip: String): Style = add("clip", clip)
    fun position(position: String): Style = add("position", position)

    // Transitions properties
    fun transitionsDelay(delay: String): Style = add("transitions-delay", delay)
    fun transitionsDuration(duration: String): Style = add("transitions-duration", duration)
    fun transitionsProperty(property: String): Style = add("transitions-property", property)
    fun transitionTimingFunction(timingFunction: String): Style = add("transition-timing-function", timingFunction)

    // 3D / 2D Transform properties
    fun backfaceVisibility(visibility: String): Style = add("backface-visibility", visibility)
    fun perspective(perspective: String): Style = add("perspective", perspective)
    fun perspectiveOrigin(origin: String): Style = add("perspective-origin", origin)
    fun transform(transform: String): Style = add("transform", transform)
    fun transformOrigin(origin: String): Style = add("transform-origin", origin)
    fun transformStyle(style: String): Style = add("transform-style", style)

    // Speech properties
    fun cueBefore(cue: String): Style = add("cue-before", cue)
    fun cueAfter(cue: String): Style = add("cue-after", cue)
    fun pause(pause: String): Style = add("pause", pause)
    fun pauseBefore(pause: String): Style = add("pause-before", pause)
    fun pauseAfter(pause: String): Style = add("pause-after", pause)
    fun phonemes(phonemes: String): Style = add("phonemes", phonemes)
    fun voiceStress(stress: String): Style = add("voice-stress", stress)
    fun voicePitch(pitch: String): Style = add("voice-pitch", pitch)
    fun voiceDuration(duration: String): Style = add("voice-duration", duration)
    fun voiceVolume(volume: String): Style = add("voice-volume", volume)
    fun voiceFamily(family: String): Style = add("voice-family", family)
    fun voiceRate(rate: String): Style = add("voice-rate", rate)
    fun speak(speak: String): Style = add("speak", speak)
    fun restBefore(rest: String): Style = add("rest-before", rest)
    fun restAfter(rest: String): Style = add("rest-after", rest)
}
