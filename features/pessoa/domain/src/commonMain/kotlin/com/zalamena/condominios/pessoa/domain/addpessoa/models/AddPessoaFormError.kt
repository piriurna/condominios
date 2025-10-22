package com.zalamena.condominios.pessoa.domain.addpessoa.models

sealed class AddPessoaFormError {

    object Nome: AddPessoaFormError()
    object Cpf: AddPessoaFormError()
    object Telefone: AddPessoaFormError()
    object Email: AddPessoaFormError()
}