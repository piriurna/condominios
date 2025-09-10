package com.zalamena.condominios.individuo.domain.models

data class Individuo(
    val id: String,
    val cpf: String,
    val nome: String,
    val email: String,
    val telefone: String
) {

    companion object {
        val dummy = Individuo(
            id = "id",
            cpf = "cpf",
            nome = "nome",
            email = "email",
            telefone = "telefone"
        )
    }
}