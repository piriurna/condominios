package com.zalamena.condominios.condominio.ui.addcondominio.models

data class AddCondominioFormUiData(
    val nome: String,
    val rua: String,
    val numero: String,
    val cep: String,
    val cidade: String,
    val estado: String
) {
    val isValid = nome.isNotBlank() && rua.isNotBlank() && numero.isNotBlank() &&
            cep.isNotBlank() && cidade.isNotBlank() && estado.isNotBlank()

    companion object {
        val dummy = AddCondominioFormUiData(
            nome = "Condomínio Teste",
            rua = "Rua das Flores",
            numero = "123",
            cep = "12345-678",
            cidade = "São Paulo",
            estado = "SP"
        )

        val BLANK = AddCondominioFormUiData(
            nome = "",
            rua = "",
            numero = "",
            cep = "",
            cidade = "",
            estado = ""
        )
    }
}
