package com.zalamena.condominios.condominio.domain.morador.usecase

interface AddMoradorUseCase {
    suspend operator fun invoke(pessoaId: String, apartamentoId: String): Result<Unit>
}
