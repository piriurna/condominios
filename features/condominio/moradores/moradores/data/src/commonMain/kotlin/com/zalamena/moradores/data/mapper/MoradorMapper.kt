package com.zalamena.moradores.data.mapper

import com.zalamena.condominios.apartamentos.data.mapper.toDomain
import com.zalamena.condominios.pessoa.data.mapper.toDomain
import com.zalamena.moradores.data.entities.MoradorWithPessoaAndApartamentoEntity
import com.zalamena.moradores.domain.models.Morador

class MoradorMapper {
    fun MoradorWithPessoaAndApartamentoEntity.toDomain(): Morador {
        return Morador(
            pessoa = pessoa.toDomain(),
            apartamento = apartamento.toDomain()
        )
    }
}