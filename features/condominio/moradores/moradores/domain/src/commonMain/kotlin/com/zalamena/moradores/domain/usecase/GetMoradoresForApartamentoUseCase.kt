package com.zalamena.moradores.domain.usecase

import com.zalamena.moradores.domain.models.Morador
import com.zalamena.moradores.domain.repository.MoradoresRepository

class GetMoradoresForApartamentoUseCase(
    private val moradoresRepository: MoradoresRepository
) {
    suspend operator fun invoke(apartamentoId: String): Result<List<Morador>> {
        return moradoresRepository.getAllMoradoresForApartamento(apartamentoId)
    }
}