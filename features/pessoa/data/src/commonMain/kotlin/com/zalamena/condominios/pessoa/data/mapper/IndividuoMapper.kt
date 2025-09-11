package com.zalamena.condominios.pessoa.data.mapper

import com.zalamena.condominios.pessoa.data.entities.PessoaEntity
import com.zalamena.condominios.pessoa.domain.models.Pessoa

fun PessoaEntity.toDomain(): Pessoa {
    return Pessoa(
        id = id,
        nome = nome,
        cpf = cpf,
        email = email,
        telefone = telefone
    )
}


fun Pessoa.toEntity(): PessoaEntity {
    return PessoaEntity(
        id = id,
        nome = nome,
        cpf = cpf,
        email = email,
        telefone = telefone
    )
}