package com.zalamena.condominios.individuo.domain.usecase

import com.zalamena.condominios.individuo.domain.models.Individuo
import com.zalamena.condominios.individuo.domain.repository.IndividuoRepository

class GetIndividuoUseCase(
    private val individuoRepository: IndividuoRepository
) {
    suspend operator fun invoke(cpf: String): Result<Individuo> {
        return individuoRepository.getIndividuo(cpf)
    }
}