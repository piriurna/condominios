package com.zalamena.condominios.condominio.domain.condominio.models

data class Endereco(
    val rua: String,
    val numero: String,
    val cep: String,
    val cidade: String,
    val estado: String
) {
    companion object {
        val dummy = Endereco(
            rua = "Rua",
            numero = "123",
            cep = "12123123",
            cidade = "cidade",
            estado = "estado"
        )
    }
}