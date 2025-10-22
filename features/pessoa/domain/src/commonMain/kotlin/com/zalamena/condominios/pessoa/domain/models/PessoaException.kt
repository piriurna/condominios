package com.zalamena.condominios.pessoa.domain.models

sealed class PessoaException(override val message: String) : Exception(message) {

    object PessoaNotFoundException: PessoaException("Pessoa Não Encontrado")
    object DuplicatePessoaException: PessoaException("Pessoa já existe")
}