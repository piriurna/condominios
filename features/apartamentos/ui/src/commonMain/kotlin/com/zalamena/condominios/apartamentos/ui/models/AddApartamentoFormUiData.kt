package com.zalamena.condominios.apartamentos.ui.models

data class AddApartamentoFormUiData(
    val numero: String,
    val andar: String
) {

    val isValid = numero.isNotBlank() && andar.isNotBlank()

    companion object {
        val dummy = AddApartamentoFormUiData(
            numero = "101",
            andar = "1"
        )

        val BLANK = AddApartamentoFormUiData(
            numero = "",
            andar = ""
        )
    }
}