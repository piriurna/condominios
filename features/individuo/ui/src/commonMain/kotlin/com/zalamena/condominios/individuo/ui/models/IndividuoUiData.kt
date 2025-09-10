package com.zalamena.condominios.individuo.ui.models

interface IndividuoUiProperties {
    val id: String
    val nome: String
    val cpf: String
}

data class IndividuoUiData(
    override val id: String,
    override val nome: String,
    override val cpf: String
): IndividuoUiProperties {

    companion object {
        val dummy = IndividuoUiData(
            id = "id",
            nome = "nome",
            cpf = "cpf"
        )
    }
}