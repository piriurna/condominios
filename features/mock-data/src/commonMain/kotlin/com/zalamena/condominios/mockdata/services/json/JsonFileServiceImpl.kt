package com.zalamena.condominios.mockdata.services.json

import com.zalamena.condominios.mockdata.services.file.FileService
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.serializer

class JsonFileServiceImpl(
    private val fileService: FileService,
    private val jsonConverterService: JsonConverterService,
) : JsonFileService {

    val serializer = Json.serializersModule.serializer<String>()

    override suspend fun getJsonFiles(pathName: String): List<JsonObject> {
        val files = fileService.getFileNames(pathName)

        return files.map {
            jsonConverterService.toJson(
                fileService.getFileContent("$pathName/$it"),
                serializer
            )
        }
    }

    override suspend fun getJsonFromFile(fileName: String): JsonObject {
        val file = fileService.getFileContent(fileName)
        return jsonConverterService.toJson(
            file,
            serializer
        )
    }

    override suspend fun writeJsonToFile(
        fileName: String,
        content: JsonObject
    ) {
        fileService.writeFile(
            fileName,
            content.toString()
        )
    }
}