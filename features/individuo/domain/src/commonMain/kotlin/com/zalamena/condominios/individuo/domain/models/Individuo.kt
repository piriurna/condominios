package com.zalamena.condominios.individuo.domain.models


interface IndividuoProperties{
    val id: String
    val cpf: String
    val nome: String
    val email: String
    val telefone: String
}


data class Individuo(
    override val id: String,
    override val cpf: String,
    override val nome: String,
    override val email: String,
    override val telefone: String
): IndividuoProperties {

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