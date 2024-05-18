package de.allround.kotlinweb.api.html.tags

import de.allround.kotlinweb.api.html.Component

class Title(val title: String) : Component<Title>(type = "title", innerHTML = title)