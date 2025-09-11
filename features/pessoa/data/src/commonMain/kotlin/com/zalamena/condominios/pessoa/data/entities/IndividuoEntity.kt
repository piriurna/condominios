package com.zalamena.condominios.pessoa.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("Pessoa")
data class PessoaEntity(
    @PrimaryKey
    val id: String,
    val nome: String,
    val cpf: String,
    val email: String,
    val telefone: String
) {

    companion object {
        val dummy = PessoaEntity(
            id = "1",
            nome = "nome",
            cpf = "cpf",
            email = "email",
            telefone = "telefone"
        )

    }
}