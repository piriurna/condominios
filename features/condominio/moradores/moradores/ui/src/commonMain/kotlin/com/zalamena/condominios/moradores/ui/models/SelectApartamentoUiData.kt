package com.zalamena.condominios.moradores.ui.models

data class SelectApartamentoUiData(
    val id: String,
    val numero: String,
) {

    companion object {
        val dummy = SelectApartamentoUiData(
            id = "1",
            numero = "101"
        )
    }
}