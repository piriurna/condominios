package com.zalamena.moradores.domain.usecase

import com.zalamena.moradores.domain.models.Morador
import com.zalamena.moradores.domain.repository.MoradoresRepository

class GetMoradorUseCase(
    private val moradoresRepository: MoradoresRepository
) {
    suspend operator fun invoke(cpf: String, apartamentoId: String): Result<Morador> {
        return moradoresRepository.getMorador(cpf, apartamentoId).getOrNull()?.let {
            Result.success(it)
        }?: Result.failure(Exception("Morador não encontrado"))
    }
}