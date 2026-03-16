package com.zalamena.condominios.condominio.data.apartamento.mapper

import com.zalamena.condominios.condominio.data.apartamento.entity.ApartamentoEntity
import com.zalamena.condominios.condominio.data.apartamento.entity.ApartamentoWithAllData
import com.zalamena.condominios.condominio.domain.apartamento.models.Apartamento
import com.zalamena.condominios.pessoa.data.mapper.toDomain

fun ApartamentoWithAllData.toDomain(): Apartamento {
    return Apartamento(
        id = apartamento.id,
        numero = apartamento.numero,
        andar = apartamento.andar,
        moradores = moradores.map { it.pessoa.toDomain() }
    )
}

fun ApartamentoEntity.toDomain(): Apartamento {
    return Apartamento(
        id = id,
        numero = numero,
        andar = andar,
        moradores = emptyList()
    )
}

fun Apartamento.toEntity(condominioId: String): ApartamentoEntity {
    return ApartamentoEntity(
        id = id,
        numero = numero,
        andar = andar,
        condominioId = condominioId
    )
}