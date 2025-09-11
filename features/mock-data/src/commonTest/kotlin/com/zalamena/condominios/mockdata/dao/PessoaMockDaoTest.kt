package com.zalamena.condominios.mockdata.dao

import com.zalamena.condominios.mockdata.services.json.JsonConverterService
import com.zalamena.condominios.mockdata.services.json.JsonFileService
import com.zalamena.condominios.pessoa.data.entities.PessoaEntity
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.serializer
import org.kodein.mock.Mock
import org.kodein.mock.generated.injectMocks
import org.kodein.mock.tests.TestsWithMocks
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class PessoaMockDaoTest: TestsWithMocks() {

    @Mock
    lateinit var jsonFileService: JsonFileService

    @Mock
    lateinit var jsonConverterService: JsonConverterService


    private val pessoaMockDao by lazy { PessoaMockDao(jsonFileService, jsonConverterService) }

    val serializer = Json.serializersModule.serializer<PessoaEntity>()

    @BeforeTest
    fun setup() {
        pessoaMockDao.setSerializer(serializer)
    }


    @Test
    fun `GIVEN a valid pessoa WHEN adding it THEN should create a file with the pessoa`() = runTest {
        val jsonPessoa = getPessoaJson()

        everySuspending { jsonConverterService.toJson(PessoaEntity.dummy, serializer) } returns jsonPessoa
        everySuspending { jsonFileService.writeJsonToFile("pessoas/${PessoaEntity.dummy.id}.json",jsonPessoa) } runs {}


        pessoaMockDao.addPessoa(PessoaEntity.dummy)


        verifyWithSuspend {
            jsonConverterService.toJson(PessoaEntity.dummy, serializer)
            jsonFileService.writeJsonToFile("pessoas/${PessoaEntity.dummy.id}.json",jsonPessoa)
        }
    }

    @Test
    fun `GIVEN there is a pessoa added WHEN getting it THEN should get the corresponding pessoa`() = runTest {
        val jsonPessoa = getPessoaJson()

        everySuspending { jsonFileService.getJsonFromFile("pessoas/${PessoaEntity.dummy.id}.json") } returns jsonPessoa
        everySuspending { jsonConverterService.fromJson(jsonPessoa,serializer) } returns PessoaEntity.dummy


        val result = pessoaMockDao.getPessoa(PessoaEntity.dummy.id)


        assertEquals(PessoaEntity.dummy, result)

        verifyWithSuspend {
            jsonFileService.getJsonFromFile("pessoas/${PessoaEntity.dummy.id}.json")
            jsonConverterService.fromJson(jsonPessoa, serializer)
        }
    }

    @Test
    fun `GIVEN there is a pessoa added WHEN getting all pessoas THEN should get a list of pessoas`() = runTest {
        val jsonPessoa = getPessoaJson()

        everySuspending { jsonFileService.getJsonFiles("pessoas") } returns listOf(jsonPessoa)
        everySuspending { jsonConverterService.fromJson<PessoaEntity>(jsonPessoa, serializer) } returns PessoaEntity.dummy


        val result = pessoaMockDao.getAllPessoas()


        assertEquals(listOf(PessoaEntity.dummy), result)

        verifyWithSuspend {
            jsonFileService.getJsonFiles("pessoas")
            jsonConverterService.fromJson(jsonPessoa, serializer)
        }
    }


    private fun getPessoaJson(): JsonObject {
        return buildJsonObject {
            put("id", "1")
            put("nome", "nome")
            put("cpf", "cpf")
            put("email", "email")
            put("telefone", "telefone")
        }

    }

    override fun setUpMocks() {
        mocker.injectMocks(this)
    }
}