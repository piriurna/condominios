package com.zalamena.condominios.condominio.domain.moradores.usecase

import com.zalamena.condominios.condominio.domain.moradores.models.Morador
import com.zalamena.condominios.condominio.domain.moradores.repository.MoradoresRepository

class GetMoradoresUseCase(
    private val moradoresRepository: MoradoresRepository
) {
    suspend operator fun invoke(): Result<List<Morador>> {
        return moradoresRepository.getAllMoradores()
    }
}