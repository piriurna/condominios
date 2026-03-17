package com.zalamena.condominios.condominio.domain.porteiro.repository

fun interface PorteiroReassigner {
    suspend fun reassignPorteiro(porteiroId: String, newCondominioId: String): Result<Unit>
}
