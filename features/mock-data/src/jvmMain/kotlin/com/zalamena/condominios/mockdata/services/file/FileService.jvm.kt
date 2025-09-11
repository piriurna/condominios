package com.zalamena.condominios.mockdata.services.file

import java.io.File

actual class PlatformFile actual constructor(actual val path: String) {
    private val file = File(path)

    actual fun exists(): Boolean = file.exists()
    actual fun readText(): String = file.readText()
    actual fun writeText(text: String) = file.writeText(text)
    actual fun parent(): PlatformFile? = file.parentFile?.let { PlatformFile(it.path) }
    actual fun mkdirs() { file.mkdirs() }

    actual fun isDirectory(): Boolean = file.isDirectory
    actual fun isFile(): Boolean = file.isFile

    actual fun listFiles(): List<PlatformFile> {
        return file.listFiles()?.map { PlatformFile(it.absolutePath) } ?: emptyList()
    }

    actual fun extension() = file.extension

    actual val fileName: String = file.name
}

actual fun createPlatformFile(path: String): PlatformFile = PlatformFile(path)
actual class PlatformContext()

actual val PlatformContext.filesDirPath: String
    get() = "data" // Relative to working directory