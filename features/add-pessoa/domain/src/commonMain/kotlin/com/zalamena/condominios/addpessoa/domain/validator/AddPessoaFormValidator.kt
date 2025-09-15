package com.zalamena.condominios.addpessoa.domain.validator

import com.zalamena.condominios.addpessoa.domain.models.AddPessoaForm
import com.zalamena.condominios.addpessoa.domain.models.AddPessoaFormError

interface AddPessoaFormValidator {

    suspend fun validate(form: AddPessoaForm): List<AddPessoaFormError>
}