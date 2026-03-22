package com.zalamena.condominios.condominio.domain.morador.usecase

import com.zalamena.condominios.condominio.domain.morador.model.MoradorException
import com.zalamena.condominios.condominio.domain.morador.model.MoradorTipo
import com.zalamena.condominios.condominio.domain.morador.repository.MoradoresRepository

interface AddMoradorUseCase {
    suspend operator fun invoke(pessoaId: String, apartamentoId: String, tipo: MoradorTipo): Result<Unit>
}

class AddMoradorUseCaseImpl(
    private val moradoresRepository: MoradoresRepository
) : AddMoradorUseCase {
    override suspend operator fun invoke(
        pessoaId: String,
        apartamentoId: String,
        tipo: MoradorTipo
    ): Result<Unit> {
        val existingResult = moradoresRepository.getMoradoresForApartamento(apartamentoId)
        val existing = existingResult.getOrElse { emptyList() }

        if (existing.any { it.pessoa.id == pessoaId }) {
            return Result.failure(MoradorException.DuplicateMoradorException)
        }

        if (tipo == MoradorTipo.PROPRIETARIO) {
            val propCount = existing.count { it.tipo == MoradorTipo.PROPRIETARIO }
            if (propCount >= 2) {
                return Result.failure(MoradorException.MaxProprietariosExceededException)
            }
        }

        return moradoresRepository.addMorador(pessoaId, apartamentoId, tipo)
    }
}
