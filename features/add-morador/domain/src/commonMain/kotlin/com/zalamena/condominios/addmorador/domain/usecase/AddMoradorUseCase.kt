package com.zalamena.condominios.addmorador.domain.usecase

interface AddMoradorUseCase {
    suspend operator fun invoke(pessoaId: String, apartamentoId: String): Result<Unit>
}