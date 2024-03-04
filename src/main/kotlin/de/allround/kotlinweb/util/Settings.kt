package de.allround.kotlinweb.util

import java.nio.file.Path
import kotlin.io.path.Path

object Settings {
    var STATIC_DIR: Path = Path("/static/")
    var STATIC_ROUTE: String = "/static"
}