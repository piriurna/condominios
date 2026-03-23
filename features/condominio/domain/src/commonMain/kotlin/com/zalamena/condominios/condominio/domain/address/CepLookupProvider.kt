package com.zalamena.condominios.condominio.domain.address

fun interface CepLookupProvider {
    suspend fun lookup(cep: String): Result<CepResult>
}
