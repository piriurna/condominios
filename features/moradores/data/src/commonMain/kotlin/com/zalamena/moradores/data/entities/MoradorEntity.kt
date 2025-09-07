package com.zalamena.moradores.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Morador")
data class MoradorEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val nome: String,
    val apartamento: String,
    val cpf: String
) {
}