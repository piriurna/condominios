package com.zalamena.condominios.condominio.domain.moradores.usecase

import com.zalamena.condominios.condominio.domain.moradores.models.ApartamentoWithMoradores
import com.zalamena.condominios.condominio.domain.moradores.repository.MoradoresRepository

class GetApartamentoWithMoradoresUseCase(
    private val moradoresRepository: MoradoresRepository
) {
    suspend operator fun invoke(apartamentoId: String): Result<ApartamentoWithMoradores> {
        return moradoresRepository.getApartamentoWithMoradores(apartamentoId)
    }
}