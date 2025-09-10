package com.zalamena.condominios.individuo.domain.usecase

import com.zalamena.condominios.individuo.domain.models.Individuo
import com.zalamena.condominios.individuo.domain.models.IndividuoException
import com.zalamena.condominios.individuo.domain.repository.IndividuoRepository

class AddIndividuoUseCase(
    private val individuoRepository: IndividuoRepository
) {
    suspend operator fun invoke(individuo: Individuo): Result<Individuo> {
        val existingIndividuo = individuoRepository.getIndividuo(individuo.cpf)


        if(existingIndividuo.exceptionOrNull() == IndividuoException.IndividuoNotFoundException) {
            return individuoRepository.addIndividuo(individuo)
        }

        return Result.failure(IndividuoException.DuplicateIndividuoException)
    }
}
