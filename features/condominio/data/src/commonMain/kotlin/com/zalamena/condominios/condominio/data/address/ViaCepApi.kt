package com.zalamena.condominios.condominio.data.address

import com.zalamena.condominios.condominio.domain.address.CepResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.Serializable

class ViaCepApi(private val client: HttpClient) {

    suspend fun lookup(cep: String): Result<CepResult> = runCatching {
        val dto: ViaCepDto = client.get("https://viacep.com.br/ws/$cep/json/").body()
        if (dto.erro == true) throw IllegalStateException("CEP nao encontrado")
        CepResult(
            rua = dto.logradouro.orEmpty(),
            cidade = dto.localidade.orEmpty(),
            estado = dto.uf.orEmpty()
        )
    }
}

@Serializable
data class ViaCepDto(
    val logradouro: String? = null,
    val localidade: String? = null,
    val uf: String? = null,
    val erro: Boolean? = null
)
