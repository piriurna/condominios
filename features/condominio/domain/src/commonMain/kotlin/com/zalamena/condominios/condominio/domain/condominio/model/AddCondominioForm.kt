package com.zalamena.condominios.condominio.domain.condominio.model

data class AddCondominioForm(
    val nome: String,
    val rua: String,
    val numero: String,
    val cep: String,
    val cidade: String,
    val estado: String
) {
    companion object {
        val dummy = AddCondominioForm(
            nome = "Condomínio Teste",
            rua = "Rua das Flores",
            numero = "123",
            cep = "12345-678",
            cidade = "São Paulo",
            estado = "SP"
        )
    }
}
