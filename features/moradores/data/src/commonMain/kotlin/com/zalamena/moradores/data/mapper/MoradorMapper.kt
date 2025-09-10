package com.zalamena.moradores.data.mapper

import com.zalamena.condominios.apartamentos.data.mapper.toDomain
import com.zalamena.condominios.individuo.data.mapper.toDomain
import com.zalamena.moradores.data.entities.MoradorWithIndividuoAndApartamentoEntity
import com.zalamena.moradores.domain.models.Morador

class MoradorMapper {
    fun MoradorWithIndividuoAndApartamentoEntity.toDomain(): Morador {
        return Morador(
            individuo = individuo.toDomain(),
            apartamento = apartamento.toDomain()
        )
    }
}