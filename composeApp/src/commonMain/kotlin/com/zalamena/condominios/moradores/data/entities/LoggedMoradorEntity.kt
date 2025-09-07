package com.zalamena.condominios.moradores.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "LoggedMorador",
    foreignKeys = [
        ForeignKey(
            entity = MoradorEntity::class,
            parentColumns = ["cpf"],
            childColumns = ["moradorCpf"]
        )
    ]
)
data class LoggedMoradorEntity(
    val moradorCpf: String,
    val expiryDate: Long
)