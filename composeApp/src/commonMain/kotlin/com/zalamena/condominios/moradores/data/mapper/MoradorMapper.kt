package com.zalamena.condominios.moradores.data.mapper

import com.zalamena.condominios.moradores.data.entities.MoradorEntity
import com.zalamena.condominios.moradores.domain.models.Morador

class MoradorMapper {
    fun MoradorEntity.toDomain(): Morador {
        return Morador(
            nome = nome,
            apartamento = apartamento,
            cpf = cpf
        )
    }


    fun Morador.toEntity(): MoradorEntity {
        return MoradorEntity(
            id = 0,
            nome = nome,
            apartamento = apartamento,
            cpf = cpf
        )
    }
}