package com.zalamena.condominios.condominio.domain.porteiro.usecase

import com.zalamena.condominios.condominio.domain.condominio.repository.CondominioRepository
import com.zalamena.condominios.condominio.domain.porteiro.repository.PorteiroReassigner

class ReassignPorteiroUseCase(
    private val porteiroReassigner: PorteiroReassigner,
    private val condominioRepository: CondominioRepository
) {

    suspend operator fun invoke(porteiroId: String, newCondominioId: String): Result<Unit> {
        if (porteiroId.isBlank()) {
            return Result.failure(IllegalArgumentException("porteiroId must not be blank"))
        }
        if (newCondominioId.isBlank()) {
            return Result.failure(IllegalArgumentException("newCondominioId must not be blank"))
        }

        val condominioResult = condominioRepository.getCondominio(newCondominioId)
        if (condominioResult.isFailure) {
            return Result.failure(IllegalArgumentException("Condominio not found: $newCondominioId"))
        }

        return porteiroReassigner.reassignPorteiro(porteiroId, newCondominioId)
    }
}
