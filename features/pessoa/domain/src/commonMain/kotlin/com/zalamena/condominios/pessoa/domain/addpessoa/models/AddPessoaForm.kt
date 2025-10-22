package com.zalamena.condominios.pessoa.domain.addpessoa.models

data class AddPessoaForm(
    val nome: String,
    val cpf: String,
    val email: String,
    val telefone: String
) {

    companion object {
        val dummy = AddPessoaForm(
            nome = "nome",
            cpf = "cpf",
            email = "email",
            telefone = "telefone"
        )
    }
}