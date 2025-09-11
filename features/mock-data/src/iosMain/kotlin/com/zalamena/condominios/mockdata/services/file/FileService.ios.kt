package com.zalamena.condominios.mockdata.services.file

actual class PlatformFile actual constructor(actual val path: String) {
    actual fun exists(): Boolean {
        TODO("Not yet implemented")
    }

    actual fun readText(): String {
        TODO("Not yet implemented")
    }

    actual fun writeText(text: String) {
    }

    actual fun parent(): PlatformFile? {
        TODO("Not yet implemented")
    }

    actual fun mkdirs() {
    }

    actual fun listFiles(): List<PlatformFile> {
        TODO("Not yet implemented")
    }

    actual fun isDirectory(): Boolean {
        TODO("Not yet implemented")
    }

    actual fun isFile(): Boolean {
        TODO("Not yet implemented")
    }

    actual fun extension(): String {
        TODO("Not yet implemented")
    }

    actual val fileName: String
        get() = TODO("Not yet implemented")
}