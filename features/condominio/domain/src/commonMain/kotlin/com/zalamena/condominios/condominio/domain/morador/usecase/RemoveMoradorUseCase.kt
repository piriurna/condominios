package com.zalamena.condominios.condominio.domain.morador.usecase

import com.zalamena.condominios.condominio.domain.morador.repository.MoradoresRepository

interface RemoveMoradorUseCase {
    suspend operator fun invoke(pessoaId: String, apartamentoId: String): Result<Unit>
}

class RemoveMoradorUseCaseImpl(
    private val moradoresRepository: MoradoresRepository
) : RemoveMoradorUseCase {
    override suspend operator fun invoke(pessoaId: String, apartamentoId: String): Result<Unit> {
        if (pessoaId.isBlank()) return Result.failure(IllegalArgumentException("pessoaId cannot be blank"))
        if (apartamentoId.isBlank()) return Result.failure(IllegalArgumentException("apartamentoId cannot be blank"))
        return moradoresRepository.removeMorador(pessoaId, apartamentoId)
    }
}
