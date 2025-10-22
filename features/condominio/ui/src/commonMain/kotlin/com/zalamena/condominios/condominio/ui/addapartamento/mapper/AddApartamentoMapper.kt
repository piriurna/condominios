package com.zalamena.condominios.condominio.ui.addapartamento.mapper

import com.zalamena.condominios.condominio.domain.addapartamento.models.AddApartamentoForm
import com.zalamena.condominios.condominio.ui.addapartamento.models.AddApartamentoFormUiData

fun AddApartamentoFormUiData.toDomain(): AddApartamentoForm {
    return AddApartamentoForm(
        numero = numero,
        andar = andar,
    )

}