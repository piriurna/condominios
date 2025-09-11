package com.zalamena.condominios.pessoa.domain.usecase

import com.zalamena.condominios.pessoa.domain.models.Pessoa
import com.zalamena.condominios.pessoa.domain.models.PessoaException
import com.zalamena.condominios.pessoa.domain.repository.PessoaRepository

class AddPessoaUseCase(
    private val pessoaRepository: PessoaRepository
) {
    suspend operator fun invoke(pessoa: Pessoa): Result<Pessoa> {
        val existingPessoa = pessoaRepository.getPessoa(pessoa.cpf)


        if(existingPessoa.exceptionOrNull() == PessoaException.PessoaNotFoundException) {
            return pessoaRepository.addPessoa(pessoa)
        }

        return Result.failure(PessoaException.DuplicatePessoaException)
    }
}
