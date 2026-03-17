package com.zalamena.condominios.condominio.domain.porteiro.repository

fun interface PorteiroDeleter {
    suspend fun deletePorteiro(porteiroId: String): Result<Unit>
}
