package com.zalamena.condominios.apartamentos.domain.usecase

import com.zalamena.condominios.apartamentos.domain.models.Apartamento
import com.zalamena.condominios.apartamentos.domain.repository.ApartamentosRepository

class GetApartamentosUseCase(
    private val apartamentosRepository: ApartamentosRepository
) {


    suspend operator fun invoke(): Result<List<Apartamento>> {
        return apartamentosRepository.getApartamentos()
    }
}