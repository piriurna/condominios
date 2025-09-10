package com.zalamena.condominios.moradores.ui.models

import com.zalamena.condominios.apartamentos.ui.models.ApartamentoUiData
import com.zalamena.condominios.individuo.ui.models.IndividuoUiData
import com.zalamena.condominios.individuo.ui.models.IndividuoUiProperties

data class MoradorUiData(
    val morador: IndividuoUiData,
    val apartamento: ApartamentoUiData
): IndividuoUiProperties by morador {

    companion object {
        val dummy = MoradorUiData(
            morador = IndividuoUiData.dummy,
            apartamento = ApartamentoUiData.dummy
        )
    }
}