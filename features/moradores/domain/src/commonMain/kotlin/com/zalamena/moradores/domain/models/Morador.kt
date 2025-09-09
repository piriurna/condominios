package com.zalamena.moradores.domain.models

data class Morador(
    val nome: String,
    val apartamento: String,
    val cpf: String

) {

    companion object {

        val dummy = Morador(
            nome = "nome",
            apartamento = "apartamento",
            cpf = "cpf"
        )
    }
}