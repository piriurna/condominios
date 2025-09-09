package com.zalamena.condominios.moradores.ui.models

data class MoradorUiData(
    val nome: String,
    val apartamento: String
) {

    companion object {
        val dummy = MoradorUiData(
            nome = "nome",
            apartamento = "703"
        )
    }
}