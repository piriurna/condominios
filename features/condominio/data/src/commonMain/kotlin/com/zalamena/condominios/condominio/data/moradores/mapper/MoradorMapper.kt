package com.zalamena.condominios.condominio.data.moradores.mapper

import com.zalamena.condominios.condominio.data.apartamento.mapper.toDomain
import com.zalamena.condominios.condominio.data.moradores.entities.MoradorWithPessoaAndApartamentoEntity
import com.zalamena.condominios.condominio.domain.moradores.models.Morador
import com.zalamena.condominios.pessoa.data.mapper.toDomain

fun MoradorWithPessoaAndApartamentoEntity.toDomain(): Morador {
    return Morador(
        pessoa = pessoa.toDomain(),
        apartamento = apartamento.toDomain()
    )
}