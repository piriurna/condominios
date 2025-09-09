package com.zalamena.condominios.apartamentos.data.entity

import androidx.room.Entity


@Entity(tableName = "Apartamento")
data class ApartamentoEntity(
    val id: String,
    val numero: String,
    val andar: String
)