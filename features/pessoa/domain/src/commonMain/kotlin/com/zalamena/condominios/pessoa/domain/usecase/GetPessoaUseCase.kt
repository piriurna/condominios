package com.zalamena.condominios.pessoa.domain.usecase

import com.zalamena.condominios.pessoa.domain.models.Pessoa
import com.zalamena.condominios.pessoa.domain.repository.PessoaRepository

interface GetPessoaUseCase {
    suspend operator fun invoke(id: String): Result<Pessoa>
}

class GetPessoaUseCaseImpl(
    private val pessoaRepository: PessoaRepository
) : GetPessoaUseCase {
    override suspend operator fun invoke(id: String): Result<Pessoa> {
        return pessoaRepository.getPessoa(id)
    }
}
