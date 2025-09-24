package com.zalamena.condominios.addpessoa.domain.models

sealed class AddPessoaFormError {

    object Nome: AddPessoaFormError()
    object Cpf: AddPessoaFormError()
    object Telefone: AddPessoaFormError()
    object Email: AddPessoaFormError()
}