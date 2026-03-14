package com.zalamena.condominios.condominio.domain.condominio.models

import com.zalamena.condominios.condominio.domain.apartamento.models.Apartamento

data class Condominio(
    val id: String,
    val nome: String,
    val endereco: Endereco,
    val apartamentos: List<Apartamento>
) {


    companion object {
        val dummy = Condominio(
            id = "id",
            nome = "nome",
            endereco = Endereco.dummy,
            apartamentos = listOf(Apartamento.dummy),
        )
    }
}
