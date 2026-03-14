package com.zalamena.condominios.condominio.domain.addmorador.usecase

import com.zalamena.condominios.condominio.domain.moradores.repository.MoradoresRepository

class AddMoradorUseCaseImpl(
    private val moradoresRepository: MoradoresRepository
): AddMoradorUseCase {
    override suspend operator fun invoke(pessoaId: String, apartamentoId: String): Result<Unit> {
        return runCatching {
            moradoresRepository.addMorador(pessoaId, apartamentoId)
        }
    }
}