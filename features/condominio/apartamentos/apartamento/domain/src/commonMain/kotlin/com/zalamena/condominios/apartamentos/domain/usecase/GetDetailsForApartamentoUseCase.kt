package com.zalamena.condominios.apartamentos.domain.usecase

import com.zalamena.condominios.apartamentos.domain.models.Apartamento
import com.zalamena.condominios.apartamentos.domain.repository.ApartamentosRepository

class GetDetailsForApartamentoUseCase(
    private val apartamentosRepository: ApartamentosRepository
) {
    suspend operator fun invoke(apartamentoId: String): Result<Apartamento> {
        return apartamentosRepository.getApartamento(apartamentoId)
    }
}