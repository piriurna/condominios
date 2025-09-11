package com.zalamena.condominios.pessoa.domain.usecase

import com.zalamena.condominios.pessoa.domain.models.Pessoa
import com.zalamena.condominios.pessoa.domain.repository.PessoaRepository

class GetPessoaUseCase(
    private val pessoaRepository: PessoaRepository
) {
    suspend operator fun invoke(cpf: String): Result<Pessoa> {
        return pessoaRepository.getPessoa(cpf)
    }
}