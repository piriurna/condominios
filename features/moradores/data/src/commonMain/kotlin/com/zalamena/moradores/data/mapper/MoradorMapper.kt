package com.zalamena.moradores.data.mapper

import com.zalamena.moradores.data.entities.MoradorEntity
import com.zalamena.moradores.domain.models.Morador

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