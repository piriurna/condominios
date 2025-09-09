package com.zalamena.moradores.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "Morador",
)
data class MoradorEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val nome: String,
    val cpf: String,
    val apartamentoId: String
) {


    companion object {
        val dummy = MoradorEntity(
            id = 1L,
            nome = "nome",
            cpf = "cpf",
            apartamentoId = "1"
        )
    }
}