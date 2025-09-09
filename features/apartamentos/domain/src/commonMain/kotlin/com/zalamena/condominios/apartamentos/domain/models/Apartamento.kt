package com.zalamena.condominios.apartamentos.domain.models

import com.zalamena.moradores.domain.models.Morador

data class Apartamento(
    val id: String,
    val numero: String,
    val andar: String,
    val moradores: List<Morador>
) {

    companion object {
        val dummy = Apartamento(
            id = "id",
            numero = "numero",
            andar = "andar",
            moradores = emptyList()
        )
    }
}
