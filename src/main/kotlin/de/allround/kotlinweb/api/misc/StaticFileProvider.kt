package de.allround.kotlinweb.api.misc

import io.vertx.core.http.HttpServerResponse
import java.nio.file.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.isRegularFile
import kotlin.io.path.notExists

object StaticFileProvider {
    fun provide(path: Path, response: HttpServerResponse) {
        if (path.notExists()) return
        if (!path.isRegularFile()) return
        if (response.ended() || response.headWritten()) return

        response.sendFile(path.absolutePathString())
    }
}