package com.zalamena.condominios.mockdata.services.file



class FileServiceImpl(
    private val context: PlatformContext
) : FileService {
    override fun writeFile(fileName: String, content: String) {
        val file = PlatformFile("${context.filesDirPath}/$fileName")
        file.parent()?.mkdirs()
        file.writeText(content)
    }

    override fun getFileContent(filePath: String): String {
        val baseDir = createPlatformFile(context.filesDirPath)
        return createPlatformFile("${baseDir.path}/$filePath").readText()
    }

    override fun getFileNames(folderPath: String): List<String> {
        val baseDir = createPlatformFile(context.filesDirPath)
        val folder = createPlatformFile("${baseDir.path}/$folderPath")

        if (!folder.exists() || !folder.isDirectory()) {
            return emptyList()
        }

        return folder.listFiles()
            .filter { it.isFile() && it.extension() == "json" }
            .map { it.fileName }
    }
}