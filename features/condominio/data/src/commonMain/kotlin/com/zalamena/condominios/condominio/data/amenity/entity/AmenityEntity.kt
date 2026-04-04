package com.zalamena.condominios.condominio.data.amenity.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Amenity")
data class AmenityEntity(
    @PrimaryKey
    val id: String,
    val condominioId: String,
    val nome: String,
    val descricao: String,
    val capacidade: Int?,
    val precoUso: Double?,
    val requerAprovacaoSindico: Boolean
)
