package com.zalamena.condominios.pessoa.domain.models


interface PessoaProperties{
    val id: String
    val cpf: String
    val nome: String
    val email: String
    val telefone: String
}


data class Pessoa(
    override val id: String,
    override val cpf: String,
    override val nome: String,
    override val email: String,
    override val telefone: String
): PessoaProperties {

    companion object {
        val dummy = Pessoa(
            id = "id",
            cpf = "cpf",
            nome = "nome",
            email = "email",
            telefone = "telefone"
        )
    }
}