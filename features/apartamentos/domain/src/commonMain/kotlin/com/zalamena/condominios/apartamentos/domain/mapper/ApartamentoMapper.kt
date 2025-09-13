package com.zalamena.condominios.apartamentos.domain.mapper

import com.zalamena.condominios.apartamentos.domain.models.AddApartamentoForm
import com.zalamena.condominios.apartamentos.domain.models.Apartamento

fun AddApartamentoForm.toApartamento(id: String) =
    Apartamento(
        id = id,
        numero = numero,
        andar = andar
    )