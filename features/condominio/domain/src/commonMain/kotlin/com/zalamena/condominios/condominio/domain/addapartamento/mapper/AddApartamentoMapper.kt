package com.zalamena.condominios.condominio.domain.addapartamento.mapper

import com.zalamena.condominios.condominio.domain.addapartamento.models.AddApartamentoForm
import com.zalamena.condominios.condominio.domain.apartamento.models.Apartamento

fun AddApartamentoForm.toApartamento(id: String) =
    Apartamento(
        id = id,
        numero = numero,
        andar = andar,
        moradores = emptyList()
    )