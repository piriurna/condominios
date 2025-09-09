package com.zalamena.condominios.moradores.ui.mapper

import com.zalamena.condominios.moradores.ui.models.MoradorUiData
import com.zalamena.moradores.domain.models.Morador

fun Morador.toUi(): MoradorUiData {
    return MoradorUiData(
        nome = nome,
        apartamento = apartamentoId
    )
}