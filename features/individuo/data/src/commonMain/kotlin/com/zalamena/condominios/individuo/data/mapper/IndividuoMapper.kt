package com.zalamena.condominios.individuo.data.mapper

import com.zalamena.condominios.individuo.data.entities.IndividuoEntity
import com.zalamena.condominios.individuo.domain.models.Individuo

fun IndividuoEntity.toDomain(): Individuo {
    return Individuo(
        id = id,
        nome = nome,
        cpf = cpf,
        email = email,
        telefone = telefone
    )
}


fun Individuo.toEntity(): IndividuoEntity {
    return IndividuoEntity(
        id = id,
        nome = nome,
        cpf = cpf,
        email = email,
        telefone = telefone
    )
}