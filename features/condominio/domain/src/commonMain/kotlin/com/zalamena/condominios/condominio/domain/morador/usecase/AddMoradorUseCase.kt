package com.zalamena.condominios.condominio.domain.morador.usecase

import com.zalamena.condominios.condominio.domain.morador.repository.MoradoresRepository

interface AddMoradorUseCase {
    suspend operator fun invoke(pessoaId: String, apartamentoId: String): Result<Unit>
}

class AddMoradorUseCaseImpl(
    private val moradoresRepository: MoradoresRepository
) : AddMoradorUseCase {
    override suspend operator fun invoke(pessoaId: String, apartamentoId: String): Result<Unit> {
        return runCatching {
            moradoresRepository.addMorador(pessoaId, apartamentoId)
        }
    }
}
