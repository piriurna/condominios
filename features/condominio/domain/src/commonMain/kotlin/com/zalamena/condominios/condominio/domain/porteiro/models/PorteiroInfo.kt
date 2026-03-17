package com.zalamena.condominios.condominio.domain.porteiro.models

data class PorteiroInfo(
    val id: String,
    val name: String,
    val cpf: String,
    val email: String,
    val condominioId: String
)
