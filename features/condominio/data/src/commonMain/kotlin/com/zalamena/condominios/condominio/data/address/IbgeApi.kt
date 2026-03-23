package com.zalamena.condominios.condominio.data.address

import com.zalamena.condominios.condominio.domain.address.Cidade
import com.zalamena.condominios.condominio.domain.address.Estado
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.Serializable

class IbgeApi(private val client: HttpClient) {

    suspend fun getEstados(): Result<List<Estado>> = runCatching {
        val response: List<EstadoDto> =
            client.get("https://servicodados.ibge.gov.br/api/v1/localidades/estados?orderBy=nome").body()
        response.map { Estado(id = it.id, sigla = it.sigla, nome = it.nome) }
    }

    suspend fun getCidades(uf: String): Result<List<Cidade>> = runCatching {
        val response: List<CidadeDto> =
            client.get("https://servicodados.ibge.gov.br/api/v1/localidades/estados/$uf/municipios?orderBy=nome").body()
        response.map { Cidade(id = it.id, nome = it.nome) }
    }
}

@Serializable
data class EstadoDto(val id: Int, val sigla: String, val nome: String)

@Serializable
data class CidadeDto(val id: Long, val nome: String)
