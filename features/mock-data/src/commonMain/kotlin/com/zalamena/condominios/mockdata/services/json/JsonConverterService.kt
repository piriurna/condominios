package com.zalamena.condominios.mockdata.services.json

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.json.JsonObject

interface JsonConverterService {

    suspend fun <T> fromJson(json: JsonObject, deserializer: DeserializationStrategy<T>): T

    suspend fun <T> toJson(obj: T, serializer: SerializationStrategy<T>): JsonObject
}