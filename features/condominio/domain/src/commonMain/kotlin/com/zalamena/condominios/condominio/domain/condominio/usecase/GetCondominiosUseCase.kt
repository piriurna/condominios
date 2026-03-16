package com.zalamena.condominios.condominio.domain.condominio.usecase

import com.zalamena.condominios.condominio.domain.condominio.models.Condominio
import com.zalamena.condominios.condominio.domain.condominio.repository.CondominioRepository

class GetCondominiosUseCase(
    private val condominioRepository: CondominioRepository
) {
    suspend operator fun invoke(): Result<List<Condominio>> =
        condominioRepository.getCondominios()
}
