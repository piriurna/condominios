package com.zalamena.condominios.addapartamento.domain.mapper

import com.zalamena.condominios.addapartamento.domain.models.AddApartamentoForm
import com.zalamena.condominios.apartamentos.domain.models.Apartamento

fun AddApartamentoForm.toApartamento(id: String) =
    Apartamento(
        id = id,
        numero = numero,
        andar = andar
    )