package com.zalamena.condominios.addpessoa.domain.models

sealed class AddPessoaException(message: String? = null): Exception(message) {
    data class FormValidationException(val errors: List<AddPessoaFormError>): AddPessoaException()
}