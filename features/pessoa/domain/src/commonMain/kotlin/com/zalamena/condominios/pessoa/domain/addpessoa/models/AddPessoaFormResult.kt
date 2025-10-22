package com.zalamena.condominios.pessoa.domain.addpessoa.models

sealed class AddPessoaException(message: String? = null): Exception(message) {
    data class FormValidationException(val errors: List<AddPessoaFormError>): AddPessoaException()
}