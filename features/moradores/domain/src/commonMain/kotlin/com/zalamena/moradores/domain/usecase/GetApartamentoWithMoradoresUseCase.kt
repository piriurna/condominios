package com.zalamena.moradores.domain.usecase

import com.zalamena.moradores.domain.models.ApartamentoWithMoradores
import com.zalamena.moradores.domain.repository.MoradoresRepository

class GetApartamentoWithMoradoresUseCase(
    private val moradoresRepository: MoradoresRepository
) {
    suspend operator fun invoke(apartamentoId: String): Result<ApartamentoWithMoradores> {
        return moradoresRepository.getApartamentoWithMoradores(apartamentoId)
    }
}