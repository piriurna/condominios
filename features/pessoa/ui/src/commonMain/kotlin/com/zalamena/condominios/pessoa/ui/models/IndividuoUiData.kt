package com.zalamena.condominios.pessoa.ui.models

interface PessoaUiProperties {
    val id: String
    val nome: String
    val cpf: String
}

data class PessoaUiData(
    override val id: String,
    override val nome: String,
    override val cpf: String
): PessoaUiProperties {

    companion object {
        val dummy = PessoaUiData(
            id = "id",
            nome = "nome",
            cpf = "cpf"
        )
    }
}