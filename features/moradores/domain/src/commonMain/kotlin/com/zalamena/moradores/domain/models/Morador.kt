package com.zalamena.moradores.domain.models

data class Morador(
    val nome: String,
    val apartamentoId: String,
    val cpf: String

) {

    companion object {
        val dummy = Morador(
            nome = "nome",
            apartamentoId = "apartamento",
            cpf = "cpf"
        )
    }
}