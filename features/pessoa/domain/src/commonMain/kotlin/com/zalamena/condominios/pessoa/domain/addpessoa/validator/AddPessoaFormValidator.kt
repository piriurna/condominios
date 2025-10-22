package com.zalamena.condominios.pessoa.domain.addpessoa.validator

import com.zalamena.condominios.pessoa.domain.addpessoa.models.AddPessoaForm
import com.zalamena.condominios.pessoa.domain.addpessoa.models.AddPessoaFormError

interface AddPessoaFormValidator {

    suspend fun validate(form: AddPessoaForm): List<AddPessoaFormError>
}