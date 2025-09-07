package com.zalamena.condominios.moradores.domain.usecase

import com.zalamena.condominios.moradores.domain.models.Morador
import com.zalamena.condominios.moradores.domain.repository.MoradoresRepository

class GetMoradoresUseCase(
    private val moradoresRepository: MoradoresRepository
) {
    operator fun invoke(): List<Morador> {
        return moradoresRepository.getAllMoradores()
    }
}