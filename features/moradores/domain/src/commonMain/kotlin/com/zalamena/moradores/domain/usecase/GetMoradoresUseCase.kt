package com.zalamena.moradores.domain.usecase

import com.zalamena.moradores.domain.models.Morador
import com.zalamena.moradores.domain.repository.MoradoresRepository

class GetMoradoresUseCase(
    private val moradoresRepository: MoradoresRepository
) {
    operator fun invoke(): List<Morador> {
        return moradoresRepository.getAllMoradores()
    }
}