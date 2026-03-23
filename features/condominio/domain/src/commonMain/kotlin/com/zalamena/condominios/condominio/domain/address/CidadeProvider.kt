package com.zalamena.condominios.condominio.domain.address

fun interface CidadeProvider {
    suspend fun getCidades(uf: String): Result<List<Cidade>>
}
