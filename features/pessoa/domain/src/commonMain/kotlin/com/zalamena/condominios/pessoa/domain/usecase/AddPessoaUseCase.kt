package com.zalamena.condominios.pessoa.domain.usecase

import com.zalamena.condominios.pessoa.domain.mapper.toPessoa
import com.zalamena.condominios.pessoa.domain.models.AddPessoaForm
import com.zalamena.condominios.pessoa.domain.models.Pessoa
import com.zalamena.condominios.pessoa.domain.repository.PessoaRepository

class AddPessoaUseCase(
    private val pessoaRepository: PessoaRepository,
) {
    suspend operator fun invoke(pessoaForm: AddPessoaForm): Result<Pessoa> {
        val pessoaIdResult = pessoaRepository.createPessoaId(
            pessoaForm.cpf,
            pessoaForm.nome,
            pessoaForm.email,
            pessoaForm.telefone
        )

        return if(pessoaIdResult.isSuccess) {
            val id = pessoaIdResult.getOrThrow()

            pessoaRepository.addPessoa(pessoaForm.toPessoa(id))
        } else {
            return Result.failure(pessoaIdResult.exceptionOrNull()!!)
        }
    }
}
