package com.zalamena.condominios.apartamentos.ui.mapper

import com.zalamena.condominios.apartamentos.ui.models.ApartamentoUiData
import com.zalamena.condominios.condominio.domain.apartamento.models.Apartamento

fun Apartamento.toUi(): ApartamentoUiData {
    return ApartamentoUiData(
        id = id,
        numero = numero,
        andar = andar,
    )
}