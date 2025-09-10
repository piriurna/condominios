package com.zalamena.moradores.domain.models

import com.zalamena.condominios.apartamentos.domain.models.Apartamento
import com.zalamena.condominios.individuo.domain.models.Individuo
import com.zalamena.condominios.individuo.domain.models.IndividuoProperties

data class Morador(
    val individuo: Individuo,
    val apartamento: Apartamento
): IndividuoProperties by individuo {

    companion object {
        val dummy = Morador(
            individuo = Individuo.dummy,
            apartamento = Apartamento.dummy
        )
    }
}