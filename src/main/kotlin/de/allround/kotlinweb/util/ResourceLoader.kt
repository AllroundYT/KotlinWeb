package de.allround.kotlinweb.util

import java.io.BufferedInputStream
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path

object ResourceLoader {
    fun copyResourcesIntoWorkingDirectory(vararg resources: String) {
        for (resource in resources) {
            println("Loading resource... $resource")

            val path = Path.of("resources", *if (resource.startsWith("/")) resource.substring(1)
                .split("/".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray() else resource.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            )
            try {
                if (path.parent != null) {
                    Files.createDirectories(path.parent)
                }
                Files.deleteIfExists(path)
                Files.createFile(path)
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
            try {
                ResourceLoader::class.java.getResourceAsStream(resource).use { inputStream ->
                    if (inputStream != null) {
                        val bufferedInputStream = BufferedInputStream(inputStream)
                        val fileContent = bufferedInputStream.readAllBytes()
                        Files.write(path, fileContent)
                        println("Writing file ${path.toAbsolutePath()}... (${fileContent.size} bytes)")
                    } else {
                        Files.write(path, ByteArray(0))
                        println("Writing file ${path.toAbsolutePath()}... (0 bytes - no InputStream)")
                    }
                }
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        }
    }
}
