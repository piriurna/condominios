package com.zalamena.condominios.condominio.domain.addmorador.usecase

interface AddMoradorUseCase {
    suspend operator fun invoke(pessoaId: String, apartamentoId: String): Result<Unit>
}