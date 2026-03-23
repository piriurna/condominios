package com.zalamena.condominios.condominio.domain.address

fun interface EstadoProvider {
    suspend fun getEstados(): Result<List<Estado>>
}
