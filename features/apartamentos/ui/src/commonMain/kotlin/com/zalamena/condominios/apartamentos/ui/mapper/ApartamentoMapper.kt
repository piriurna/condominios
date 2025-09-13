package com.zalamena.condominios.apartamentos.ui.mapper

import com.zalamena.condominios.apartamentos.domain.models.AddApartamentoForm
import com.zalamena.condominios.apartamentos.domain.models.Apartamento
import com.zalamena.condominios.apartamentos.ui.models.AddApartamentoFormUiData
import com.zalamena.condominios.apartamentos.ui.models.ApartamentoUiData

fun Apartamento.toUi(): ApartamentoUiData {
    return ApartamentoUiData(
        id = id,
        numero = numero,
        andar = andar,
    )
}


fun AddApartamentoFormUiData.toDomain(): AddApartamentoForm {
    return AddApartamentoForm(
        numero = numero,
        andar = andar,
    )

}