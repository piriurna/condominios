package com.zalamena.condominios.condominio.domain.moradores.models

import com.zalamena.condominios.condominio.domain.apartamento.models.Apartamento
import com.zalamena.condominios.condominio.domain.apartamento.models.ApartamentoProperties
import com.zalamena.condominios.pessoa.domain.models.Pessoa

data class ApartamentoWithMoradores(
    val apartamento: Apartamento,
    val moradores: List<Pessoa>
): ApartamentoProperties by apartamento {


    companion object {
        val dummy = ApartamentoWithMoradores(
            apartamento = Apartamento.dummy,
            moradores = listOf(Pessoa.dummy)
        )
    }
}