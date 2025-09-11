package com.zalamena.condominios.moradores.ui.mapper

import com.zalamena.condominios.apartamentos.ui.mapper.toUi
import com.zalamena.condominios.pessoa.ui.mapper.toUi
import com.zalamena.condominios.moradores.ui.models.MoradorUiData
import com.zalamena.moradores.domain.models.Morador

fun Morador.toUi(): MoradorUiData {
    return MoradorUiData(
        morador = pessoa.toUi(),
        apartamento = apartamento.toUi(),
    )
}