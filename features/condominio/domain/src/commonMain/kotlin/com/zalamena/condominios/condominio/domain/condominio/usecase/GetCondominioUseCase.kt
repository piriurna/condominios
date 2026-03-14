package com.zalamena.condominios.condominio.domain.condominio.usecase

import com.zalamena.condominios.condominio.domain.condominio.models.Condominio
import com.zalamena.condominios.condominio.domain.condominio.repository.CondominioRepository

class GetCondominioUseCase(
    private val condominioRepository: CondominioRepository
) {

    suspend operator fun invoke(condominioId: String): Result<Condominio> {
        return condominioRepository.getCondominio(condominioId)
    }
}