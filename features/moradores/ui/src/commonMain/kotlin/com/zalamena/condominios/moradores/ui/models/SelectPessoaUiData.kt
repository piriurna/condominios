package com.zalamena.condominios.moradores.ui.models

data class SelectPessoaUiData(
    val id: String,
    val nome: String,
    val cpf: String?
) {

    companion object {
        val dummy = SelectPessoaUiData(
            id = "1",
            nome = "João da Silva",
            cpf = "123.456.789-00"
        )

    }
}