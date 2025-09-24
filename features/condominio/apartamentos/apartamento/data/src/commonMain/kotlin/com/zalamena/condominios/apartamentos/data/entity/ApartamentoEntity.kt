package com.zalamena.condominios.apartamentos.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Apartamento")
data class ApartamentoEntity(
    @PrimaryKey
    val id: String,
    val numero: String,
    val andar: String
) {
    companion object {
        val dummy = ApartamentoEntity(
            id = "1",
            numero = "1",
            andar = "1"
        )

    }
}