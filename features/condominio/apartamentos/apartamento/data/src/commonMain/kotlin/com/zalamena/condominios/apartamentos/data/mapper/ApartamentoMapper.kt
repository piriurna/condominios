package com.zalamena.condominios.apartamentos.data.mapper

import com.zalamena.condominios.apartamentos.data.entity.ApartamentoEntity
import com.zalamena.condominios.apartamentos.domain.models.Apartamento

fun ApartamentoEntity.toDomain(): Apartamento {
    return Apartamento(
        id = id,
        numero = numero,
        andar = andar,
    )
}

fun Apartamento.toEntity(): ApartamentoEntity {
    return ApartamentoEntity(
        id = id,
        numero = numero,
        andar = andar,
    )
}