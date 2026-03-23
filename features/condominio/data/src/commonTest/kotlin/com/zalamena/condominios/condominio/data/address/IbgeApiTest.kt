package com.zalamena.condominios.condominio.data.address

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class IbgeApiTest {

    private fun createClient(responseBody: String, statusCode: HttpStatusCode = HttpStatusCode.OK): HttpClient {
        val mockEngine = MockEngine {
            respond(
                content = responseBody,
                status = statusCode,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }
        return HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
    }

    @Test
    fun `GIVEN valid response WHEN getEstados THEN returns parsed estados`() = runTest {
        val json = """[
            {"id": 35, "sigla": "SP", "nome": "Sao Paulo"},
            {"id": 33, "sigla": "RJ", "nome": "Rio de Janeiro"}
        ]"""
        val api = IbgeApi(createClient(json))

        val result = api.getEstados()

        assertTrue(result.isSuccess)
        val estados = result.getOrThrow()
        assertEquals(2, estados.size)
        assertEquals("SP", estados[0].sigla)
        assertEquals("Sao Paulo", estados[0].nome)
        assertEquals(35, estados[0].id)
        assertEquals("RJ", estados[1].sigla)
    }

    @Test
    fun `GIVEN empty response WHEN getEstados THEN returns empty list`() = runTest {
        val api = IbgeApi(createClient("[]"))

        val result = api.getEstados()

        assertTrue(result.isSuccess)
        assertTrue(result.getOrThrow().isEmpty())
    }

    @Test
    fun `GIVEN valid response WHEN getCidades THEN returns parsed cidades`() = runTest {
        val json = """[
            {"id": 3550308, "nome": "Sao Paulo"},
            {"id": 3509502, "nome": "Campinas"}
        ]"""
        val api = IbgeApi(createClient(json))

        val result = api.getCidades("SP")

        assertTrue(result.isSuccess)
        val cidades = result.getOrThrow()
        assertEquals(2, cidades.size)
        assertEquals("Sao Paulo", cidades[0].nome)
        assertEquals(3550308L, cidades[0].id)
    }

    @Test
    fun `GIVEN response with extra fields WHEN getEstados THEN ignores unknown keys`() = runTest {
        val json = """[{"id": 35, "sigla": "SP", "nome": "Sao Paulo", "regiao": {"id": 3, "nome": "Sudeste"}}]"""
        val api = IbgeApi(createClient(json))

        val result = api.getEstados()

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrThrow().size)
    }
}
