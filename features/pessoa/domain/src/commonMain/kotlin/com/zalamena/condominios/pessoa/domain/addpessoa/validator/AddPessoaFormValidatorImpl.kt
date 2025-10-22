package com.zalamena.condominios.pessoa.domain.addpessoa.validator

import com.zalamena.condominios.pessoa.domain.addpessoa.models.AddPessoaForm
import com.zalamena.condominios.pessoa.domain.addpessoa.models.AddPessoaFormError

class AddPessoaFormValidatorImpl : AddPessoaFormValidator {
    override suspend fun validate(form: AddPessoaForm): List<AddPessoaFormError> {
        val errors: MutableList<AddPessoaFormError> = mutableListOf()


        if (form.nome.isBlank()) {
            errors.add(AddPessoaFormError.Nome)
        }

        if (form.email.isNotEmpty() && !form.email.matches(EMAIL_REGEX)) {
            errors.add(AddPessoaFormError.Email)
        }

        if (form.telefone.isNotBlank() && form.telefone.length < 9) {
            errors.add(AddPessoaFormError.Telefone)
        }

        if (form.cpf.isNotBlank() && form.cpf.length != 11) {
            errors.add(AddPessoaFormError.Cpf)
        }

        return errors
    }

    companion object {
        val EMAIL_REGEX = """^[\w-.]+@([\w-]+\.)+[\w-]{2,4}$""".toRegex()

    }
}