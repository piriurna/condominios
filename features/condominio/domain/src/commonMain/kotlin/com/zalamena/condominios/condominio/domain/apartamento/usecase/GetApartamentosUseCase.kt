package com.zalamena.condominios.condominio.domain.apartamento.usecase

import com.zalamena.condominios.condominio.domain.apartamento.models.Apartamento
import com.zalamena.condominios.condominio.domain.apartamento.repository.ApartamentosRepository

class GetApartamentosUseCase(
    private val apartamentosRepository: ApartamentosRepository
) {


    suspend operator fun invoke(): Result<List<Apartamento>> {
        return apartamentosRepository.getApartamentos()
    }
}