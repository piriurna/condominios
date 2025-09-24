package com.zalamena.condominios.addapartamento.domain.models

data class AddApartamentoForm(
    val numero: String,
    val andar: String
) {
    companion object {
        val dummy = AddApartamentoForm(
            numero = "101",
            andar = "1"
        )
    }
}