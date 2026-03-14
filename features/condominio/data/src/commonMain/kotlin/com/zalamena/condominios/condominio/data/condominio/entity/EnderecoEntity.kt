package com.zalamena.condominios.condominio.data.condominio.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class EnderecoEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val rua: String,
    val numero: String,
    val cep: String,
    val cidade: String,
    val estado: String
) {

    companion object {
        val dummy = EnderecoEntity(
            id = "id",
            rua = "rua",
            numero = "numero",
            cep = "cep",
            cidade = "cidade",
            estado = "estado"
        )
    }
}