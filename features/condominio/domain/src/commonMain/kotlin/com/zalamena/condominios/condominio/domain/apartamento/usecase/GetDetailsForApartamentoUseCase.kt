package com.zalamena.condominios.condominio.domain.apartamento.usecase

import com.zalamena.condominios.condominio.domain.apartamento.models.Apartamento
import com.zalamena.condominios.condominio.domain.apartamento.repository.ApartamentosRepository

class GetDetailsForApartamentoUseCase(
    private val apartamentosRepository: ApartamentosRepository
) {
    suspend operator fun invoke(apartamentoId: String): Result<Apartamento> {
        return apartamentosRepository.getApartamento(apartamentoId)
    }
}