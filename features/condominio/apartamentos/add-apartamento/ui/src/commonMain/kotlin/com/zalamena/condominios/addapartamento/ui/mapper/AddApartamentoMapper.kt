package com.zalamena.condominios.addapartamento.ui.mapper

import com.zalamena.condominios.addapartamento.ui.models.AddApartamentoFormUiData
import com.zalamena.condominios.addapartamento.domain.models.AddApartamentoForm

fun AddApartamentoFormUiData.toDomain(): AddApartamentoForm {
    return AddApartamentoForm(
        numero = numero,
        andar = andar,
    )

}