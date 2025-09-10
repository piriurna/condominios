package com.zalamena.condominios.individuo.ui.mapper

import com.zalamena.condominios.individuo.domain.models.Individuo
import com.zalamena.condominios.individuo.ui.models.IndividuoUiData

fun Individuo.toUi(): IndividuoUiData {
    return IndividuoUiData(
        id = id,
        nome = nome,
        cpf = cpf,
    )
}