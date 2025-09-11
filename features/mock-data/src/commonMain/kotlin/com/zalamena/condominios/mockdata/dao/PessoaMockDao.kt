package com.zalamena.condominios.mockdata.dao

import com.zalamena.condominios.mockdata.services.json.JsonConverterService
import com.zalamena.condominios.mockdata.services.json.JsonFileService
import com.zalamena.condominios.pessoa.data.dao.PessoaDao
import com.zalamena.condominios.pessoa.data.entities.PessoaEntity
import kotlinx.serialization.KSerializer

class PessoaMockDao(
    private val jsonFileService: JsonFileService,
    private val jsonConverterService: JsonConverterService
): PessoaDao {
    private lateinit var serializer: KSerializer<PessoaEntity>

    fun setSerializer(serializer: KSerializer<PessoaEntity>) {
        this.serializer = serializer
    }
    override suspend fun addPessoa(pessoa: PessoaEntity) {
        val pessoaJson = jsonConverterService.toJson(pessoa, serializer)


        jsonFileService.writeJsonToFile("pessoas/${pessoa.id}.json", pessoaJson)
    }

    override suspend fun getPessoa(id: String): PessoaEntity? {
        val jsonFile = jsonFileService.getJsonFromFile("pessoas/${id}.json")

        return jsonConverterService.fromJson(jsonFile, serializer)
    }

    override suspend fun getAllPessoas(): List<PessoaEntity> {
        val files = jsonFileService.getJsonFiles("pessoas")

        return files.map { jsonConverterService.fromJson(it, serializer) }
    }

}