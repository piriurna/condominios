package com.zalamena.condominios.apartamentos.domain.models


interface ApartamentoProperties {
    val id: String
    val numero: String
    val andar: String
}


data class Apartamento(
    override val id: String,
    override val numero: String,
    override val andar: String
): ApartamentoProperties {

    companion object {
        val dummy = Apartamento(
            id = "id",
            numero = "numero",
            andar = "andar"
        )
    }
}
