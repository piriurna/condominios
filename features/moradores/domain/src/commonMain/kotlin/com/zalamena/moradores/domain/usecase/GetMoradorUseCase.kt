package com.zalamena.moradores.domain.usecase

import com.zalamena.moradores.domain.models.Morador
import com.zalamena.moradores.domain.repository.MoradoresRepository

class GetMoradorUseCase(
    private val moradoresRepository: MoradoresRepository
) {
    operator fun invoke(cpf: String): Result<Morador> {
        return moradoresRepository.getMorador(cpf).getOrNull()?.let {
            Result.success(it)
        }?: Result.failure(Exception("Morador não encontrado"))
    }
}