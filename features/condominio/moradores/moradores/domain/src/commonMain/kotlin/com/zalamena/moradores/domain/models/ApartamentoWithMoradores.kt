package com.zalamena.moradores.domain.models

import com.zalamena.condominios.apartamentos.domain.models.Apartamento
import com.zalamena.condominios.apartamentos.domain.models.ApartamentoProperties
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