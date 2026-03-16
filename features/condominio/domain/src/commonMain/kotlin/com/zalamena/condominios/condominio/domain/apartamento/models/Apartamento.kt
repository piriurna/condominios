package com.zalamena.condominios.condominio.domain.apartamento.models

import com.zalamena.condominios.pessoa.domain.models.Pessoa


interface ApartamentoProperties {
    val id: String
    val numero: String
    val andar: String

    val moradores: List<Pessoa>
}


data class Apartamento(
    override val id: String,
    override val numero: String,
    override val andar: String,
    override val moradores: List<Pessoa>
): ApartamentoProperties {

    companion object {
        val dummy = Apartamento(
            id = "id",
            numero = "numero",
            andar = "andar",
            moradores = listOf(Pessoa.dummy)
        )
    }
}
