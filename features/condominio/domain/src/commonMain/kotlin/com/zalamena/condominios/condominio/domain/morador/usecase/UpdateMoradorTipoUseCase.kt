package com.zalamena.condominios.condominio.domain.morador.usecase

import com.zalamena.condominios.condominio.domain.morador.model.MoradorException
import com.zalamena.condominios.condominio.domain.morador.model.MoradorTipo
import com.zalamena.condominios.condominio.domain.morador.repository.MoradoresRepository

interface UpdateMoradorTipoUseCase {
    suspend operator fun invoke(pessoaId: String, apartamentoId: String, newTipo: MoradorTipo): Result<Unit>
}

class UpdateMoradorTipoUseCaseImpl(
    private val moradoresRepository: MoradoresRepository
) : UpdateMoradorTipoUseCase {
    override suspend operator fun invoke(
        pessoaId: String,
        apartamentoId: String,
        newTipo: MoradorTipo
    ): Result<Unit> {
        if (newTipo == MoradorTipo.PROPRIETARIO) {
            val existing = moradoresRepository.getMoradoresForApartamento(apartamentoId)
                .getOrElse { emptyList() }
            val otherProprietarios = existing.count { it.tipo == MoradorTipo.PROPRIETARIO && it.pessoa.id != pessoaId }
            if (otherProprietarios >= 2) {
                return Result.failure(MoradorException.MaxProprietariosExceededException)
            }
        }
        return moradoresRepository.updateMoradorTipo(pessoaId, apartamentoId, newTipo)
    }
}
