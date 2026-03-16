package com.zalamena.condominios.condominio.ui.addapartamento.mapper

import com.zalamena.condominios.condominio.domain.apartamento.model.AddApartamentoForm
import com.zalamena.condominios.condominio.ui.addapartamento.models.AddApartamentoFormUiData

fun AddApartamentoFormUiData.toDomain(condominioId: String): AddApartamentoForm {
    return AddApartamentoForm(
        condominioId = condominioId,
        numero = numero,
        andar = andar,
    )
}