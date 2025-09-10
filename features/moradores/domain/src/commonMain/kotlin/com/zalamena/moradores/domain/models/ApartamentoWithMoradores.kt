package com.zalamena.moradores.domain.models

import com.zalamena.condominios.apartamentos.domain.models.Apartamento
import com.zalamena.condominios.apartamentos.domain.models.ApartamentoProperties
import com.zalamena.condominios.individuo.domain.models.Individuo

data class ApartamentoWithMoradores(
    val apartamento: Apartamento,
    val moradores: List<Individuo>
): ApartamentoProperties by apartamento {


    companion object {
        val dummy = ApartamentoWithMoradores(
            apartamento = Apartamento.dummy,
            moradores = listOf(Individuo.dummy)
        )
    }
}