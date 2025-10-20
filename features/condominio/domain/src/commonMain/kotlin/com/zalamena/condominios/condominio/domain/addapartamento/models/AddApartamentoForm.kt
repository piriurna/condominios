package com.zalamena.condominios.condominio.domain.addapartamento.models

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