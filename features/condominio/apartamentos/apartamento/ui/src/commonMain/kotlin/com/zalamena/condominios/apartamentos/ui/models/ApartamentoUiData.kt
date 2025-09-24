package com.zalamena.condominios.apartamentos.ui.models

data class ApartamentoUiData(
    val id: String,
    val numero: String,
    val andar: String,
) {


    companion object {
        val dummy = ApartamentoUiData(
            id = "id",
            numero = "numero",
            andar = "andar"
        )
    }
}