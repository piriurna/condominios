package com.zalamena.condominios.pessoa.domain.usecase

import com.zalamena.condominios.pessoa.domain.models.Pessoa

interface GetPessoaUseCase {
    suspend operator fun invoke(id: String): Result<Pessoa>
}