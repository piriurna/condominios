package com.zalamena.condominios.addpessoa.domain.usecase

import com.zalamena.condominios.addpessoa.domain.mapper.toPessoa
import com.zalamena.condominios.addpessoa.domain.models.AddPessoaException
import com.zalamena.condominios.addpessoa.domain.models.AddPessoaForm
import com.zalamena.condominios.addpessoa.domain.validator.AddPessoaFormValidator
import com.zalamena.condominios.pessoa.domain.repository.PessoaRepository

class AddPessoaUseCaseImpl(
    private val pessoaRepository: PessoaRepository,
    private val addPessoaFormValidator: AddPessoaFormValidator
): AddPessoaUseCase {
    override suspend operator fun invoke(pessoaForm: AddPessoaForm): Result<String> {
        addPessoaFormValidator.validate(pessoaForm).let {
            if(it.isNotEmpty()) {
                return Result.failure(AddPessoaException.FormValidationException(it))
            }
        }

        val pessoaIdResult = pessoaRepository.createPessoaId(
            pessoaForm.cpf,
            pessoaForm.nome,
            pessoaForm.email,
            pessoaForm.telefone
        )

        return if(pessoaIdResult.isSuccess) {
            val id = pessoaIdResult.getOrThrow()
            pessoaRepository.addPessoa(pessoaForm.toPessoa(id))
            Result.success(id)
        } else {
            Result.failure(pessoaIdResult.exceptionOrNull()!!)
        }
    }
}
