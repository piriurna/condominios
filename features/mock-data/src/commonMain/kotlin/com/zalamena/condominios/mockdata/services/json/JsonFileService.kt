package com.zalamena.condominios.mockdata.services.json

import kotlinx.serialization.json.JsonObject

interface JsonFileService {

    suspend fun getJsonFiles(pathName: String): List<JsonObject>

    suspend fun getJsonFromFile(fileName: String): JsonObject


    suspend fun writeJsonToFile(fileName: String, content: JsonObject)
}