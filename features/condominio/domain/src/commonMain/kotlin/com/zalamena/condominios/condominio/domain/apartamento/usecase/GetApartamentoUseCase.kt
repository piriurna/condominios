package com.zalamena.condominios.condominio.domain.apartamento.usecase

import com.zalamena.condominios.condominio.domain.apartamento.models.Apartamento
import com.zalamena.condominios.condominio.domain.apartamento.repository.ApartamentosRepository

interface GetApartamentoUseCase {
    suspend operator fun invoke(apartamentoId: String): Result<Apartamento>
}

class GetApartamentoUseCaseImpl(
    private val apartamentosRepository: ApartamentosRepository
) : GetApartamentoUseCase {
    override suspend operator fun invoke(apartamentoId: String): Result<Apartamento> {
        return apartamentosRepository.getApartamento(apartamentoId)
    }
}
