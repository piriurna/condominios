package com.zalamena.condominios.mockdata.services.json

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject

class JsonConverterServiceImpl: JsonConverterService {

    override suspend fun <T> fromJson(
        json: JsonObject,
        deserializer: DeserializationStrategy<T>
    ): T {
        return Json.decodeFromJsonElement(deserializer,json)
    }

    override suspend fun <T> toJson(
        obj: T,
        serializer: SerializationStrategy<T>
    ): JsonObject {
        return Json.encodeToJsonElement(serializer, obj).jsonObject
    }

}