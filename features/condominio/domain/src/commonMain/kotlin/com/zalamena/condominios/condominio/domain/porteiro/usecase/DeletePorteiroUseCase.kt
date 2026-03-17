package com.zalamena.condominios.condominio.domain.porteiro.usecase

import com.zalamena.condominios.condominio.domain.porteiro.repository.PorteiroDeleter

class DeletePorteiroUseCase(
    private val porteiroDeleter: PorteiroDeleter
) {

    suspend operator fun invoke(porteiroId: String): Result<Unit> {
        if (porteiroId.isBlank()) {
            return Result.failure(IllegalArgumentException("porteiroId must not be blank"))
        }
        return porteiroDeleter.deletePorteiro(porteiroId)
    }
}
