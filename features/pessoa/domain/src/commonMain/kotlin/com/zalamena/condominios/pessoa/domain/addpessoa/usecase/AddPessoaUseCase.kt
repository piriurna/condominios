package com.zalamena.condominios.pessoa.domain.addpessoa.usecase

import com.zalamena.condominios.pessoa.domain.addpessoa.mapper.toPessoa
import com.zalamena.condominios.pessoa.domain.addpessoa.models.AddPessoaException
import com.zalamena.condominios.pessoa.domain.addpessoa.models.AddPessoaForm
import com.zalamena.condominios.pessoa.domain.addpessoa.validator.AddPessoaFormValidator
import com.zalamena.condominios.pessoa.domain.repository.PessoaRepository

interface AddPessoaUseCase {
    suspend operator fun invoke(pessoaForm: AddPessoaForm): Result<String>
}

class AddPessoaUseCaseImpl(
    private val pessoaRepository: PessoaRepository,
    private val addPessoaFormValidator: AddPessoaFormValidator
) : AddPessoaUseCase {
    override suspend operator fun invoke(pessoaForm: AddPessoaForm): Result<String> {
        addPessoaFormValidator.validate(pessoaForm).let {
            if (it.isNotEmpty()) {
                return Result.failure(AddPessoaException.FormValidationException(it))
            }
        }

        val pessoaIdResult = pessoaRepository.createPessoaId(
            pessoaForm.cpf,
            pessoaForm.nome,
            pessoaForm.email,
            pessoaForm.telefone
        )

        return if (pessoaIdResult.isSuccess) {
            val id = pessoaIdResult.getOrThrow()
            pessoaRepository.addPessoa(pessoaForm.toPessoa(id))
            Result.success(id)
        } else {
            Result.failure(pessoaIdResult.exceptionOrNull()!!)
        }
    }
}
