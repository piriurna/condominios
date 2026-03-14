package com.zalamena.condominios.condominio.domain.addapartamento.models

data class AddApartamentoForm(
    val condominioId: String,
    val numero: String,
    val andar: String
) {
    companion object {
        val dummy = AddApartamentoForm(
            condominioId = "condominioId",
            numero = "101",
            andar = "1"
        )
    }
}