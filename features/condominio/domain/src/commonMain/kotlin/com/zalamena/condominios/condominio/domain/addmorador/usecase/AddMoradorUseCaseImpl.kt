package com.zalamena.condominios.condominio.domain.addmorador.usecase

import com.zalamena.condominios.addmorador.domain.usecase.AddMoradorUseCase
import com.zalamena.condominios.condominio.domain.moradores.models.MoradorException
import com.zalamena.condominios.condominio.domain.moradores.repository.MoradoresRepository

class AddMoradorUseCaseImpl(
    private val moradoresRepository: MoradoresRepository
): AddMoradorUseCase {
    override suspend operator fun invoke(pessoaId: String, apartamentoId: String): Result<Unit> {
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