package com.zalamena.condominios.condominio.ui.moradores.models

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