package com.zalamena.condominios.addmorador.domain.usecase

import com.zalamena.moradores.domain.models.MoradorException
import com.zalamena.moradores.domain.repository.MoradoresRepository

class AddMoradorUseCase(
    private val moradoresRepository: MoradoresRepository
) {
    suspend operator fun invoke(pessoaId: String, apartamentoId: String): Result<Unit> {
        val existingMoradorResult = moradoresRepository.getMorador(pessoaId, apartamentoId)

        return when(val e = existingMoradorResult.exceptionOrNull()) {
            is MoradorException.MoradorNotFoundException -> {
                moradoresRepository.addMorador(pessoaId, apartamentoId)
                Result.success(Unit)
            }

            null -> {
                 Result.failure(MoradorException.DuplicateMoradorException)
            }

            else -> {
                Result.failure(e)
            }
        }
    }
}