package com.zalamena.condominios.individuo.domain.usecase

import com.zalamena.condominios.individuo.domain.models.Individuo
import com.zalamena.condominios.individuo.domain.repository.IndividuoRepository

class GetIndividuosListUseCase(
    private val individuoRepository: IndividuoRepository
) {


    suspend operator fun invoke(): Result<List<Individuo>> {
        return individuoRepository.getAllIndividos()
    }
}