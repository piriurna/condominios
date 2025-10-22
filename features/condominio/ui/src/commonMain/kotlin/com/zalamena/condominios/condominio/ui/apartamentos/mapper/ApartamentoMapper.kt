package com.zalamena.condominios.condominio.ui.apartamentos.mapper

import com.zalamena.condominios.condominio.domain.apartamento.models.Apartamento
import com.zalamena.condominios.condominio.ui.apartamentos.models.ApartamentoUiData

fun Apartamento.toUi(): ApartamentoUiData {
    return ApartamentoUiData(
        id = id,
        numero = numero,
        andar = andar,
    )
}