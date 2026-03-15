package com.zalamena.condominios.condominio.domain.condominio.usecase

import com.zalamena.condominios.condominio.domain.morador.model.Morador
import com.zalamena.condominios.condominio.domain.morador.repository.MoradoresRepository

class GetMoradoresUseCase(
    private val moradoresRepository: MoradoresRepository
) {
    suspend operator fun invoke(condominioId: String): Result<List<Morador>> =
        moradoresRepository.getMoradoresForCondominio(condominioId)
}