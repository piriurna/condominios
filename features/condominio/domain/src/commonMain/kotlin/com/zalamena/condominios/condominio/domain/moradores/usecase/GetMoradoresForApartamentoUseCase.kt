package com.zalamena.condominios.condominio.domain.moradores.usecase

import com.zalamena.condominios.condominio.domain.moradores.models.Morador
import com.zalamena.condominios.condominio.domain.moradores.repository.MoradoresRepository

class GetMoradoresForApartamentoUseCase(
    private val moradoresRepository: MoradoresRepository
) {
    suspend operator fun invoke(apartamentoId: String): Result<List<Morador>> {
        return moradoresRepository.getAllMoradoresForApartamento(apartamentoId)
    }
}