package com.zalamena.condominios.mockdata.services.file

expect class PlatformFile(path: String) {
    fun exists(): Boolean
    fun readText(): String
    fun writeText(text: String)
    fun parent(): PlatformFile?
    fun mkdirs()

    fun isDirectory(): Boolean
    fun isFile(): Boolean
    fun listFiles(): List<PlatformFile>
    fun extension(): String

    val fileName: String

    val path: String

}

expect fun createPlatformFile(path: String): PlatformFile

expect class PlatformContext

expect val PlatformContext.filesDirPath: String

interface FileService {

    fun writeFile(fileName: String, content: String)


    fun getFileContent(filePath: String): String


    fun getFileNames(folderPath: String): List<String>
}