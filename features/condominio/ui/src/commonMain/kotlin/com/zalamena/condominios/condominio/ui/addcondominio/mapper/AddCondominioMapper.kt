package com.zalamena.condominios.condominio.ui.addcondominio.mapper

import com.zalamena.condominios.condominio.domain.condominio.model.AddCondominioForm
import com.zalamena.condominios.condominio.ui.addcondominio.models.AddCondominioFormUiData

fun AddCondominioFormUiData.toDomain(): AddCondominioForm {
    return AddCondominioForm(
        nome = nome,
        rua = rua,
        numero = numero,
        cep = cep,
        cidade = cidade,
        estado = estado
    )
}
