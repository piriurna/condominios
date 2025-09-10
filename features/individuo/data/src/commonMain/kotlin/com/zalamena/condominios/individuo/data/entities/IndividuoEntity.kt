package com.zalamena.condominios.individuo.data.entities

import androidx.room.Entity

@Entity("Individuo")
data class IndividuoEntity(
    val id: String,
    val nome: String,
    val cpf: String,
    val email: String,
    val telefone: String
) {

    companion object {
        val dummy = IndividuoEntity(
            id = "1",
            nome = "nome",
            cpf = "cpf",
            email = "email",
            telefone = "telefone"
        )

    }
}