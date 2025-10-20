package com.zalamena.condominios.condominio.data.apartamento.mapper

import com.zalamena.condominios.condominio.data.apartamento.entity.ApartamentoEntity
import com.zalamena.condominios.condominio.domain.apartamento.models.Apartamento

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