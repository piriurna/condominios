package com.zalamena.condominios.condominio.domain.morador.usecase

import com.zalamena.condominios.condominio.domain.morador.model.Morador
import com.zalamena.condominios.condominio.domain.morador.repository.MoradoresRepository

class GetMoradoresForApartamentoUseCase(
    private val moradoresRepository: MoradoresRepository
) {
    suspend operator fun invoke(apartamentoId: String): Result<List<Morador>> =
        moradoresRepository.getMoradoresForApartamento(apartamentoId)
}
