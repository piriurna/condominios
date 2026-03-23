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

class ViaCepApiTest {

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
    fun `GIVEN valid CEP WHEN lookup THEN returns parsed address`() = runTest {
        val json = """{
            "cep": "01001-000",
            "logradouro": "Praca da Se",
            "complemento": "lado impar",
            "unidade": "",
            "bairro": "Se",
            "localidade": "Sao Paulo",
            "uf": "SP",
            "estado": "Sao Paulo",
            "regiao": "Sudeste",
            "ibge": "3550308",
            "gia": "1004",
            "ddd": "11",
            "siafi": "7107"
        }"""
        val api = ViaCepApi(createClient(json))

        val result = api.lookup("01001000")

        assertTrue(result.isSuccess)
        val cepResult = result.getOrThrow()
        assertEquals("Praca da Se", cepResult.rua)
        assertEquals("Sao Paulo", cepResult.cidade)
        assertEquals("SP", cepResult.estado)
    }

    @Test
    fun `GIVEN CEP not found WHEN lookup THEN returns failure`() = runTest {
        val json = """{"erro": true}"""
        val api = ViaCepApi(createClient(json))

        val result = api.lookup("00000000")

        assertTrue(result.isFailure)
        assertEquals("CEP nao encontrado", result.exceptionOrNull()?.message)
    }

    @Test
    fun `GIVEN CEP with null fields WHEN lookup THEN returns empty strings`() = runTest {
        val json = """{"logradouro": null, "localidade": null, "uf": null}"""
        val api = ViaCepApi(createClient(json))

        val result = api.lookup("01001000")

        assertTrue(result.isSuccess)
        val cepResult = result.getOrThrow()
        assertEquals("", cepResult.rua)
        assertEquals("", cepResult.cidade)
        assertEquals("", cepResult.estado)
    }
}
