package com.zalamena.condominios.condominio.ui.porteiro.list.models

import com.zalamena.condominios.condominio.domain.porteiro.models.PorteiroInfo

data class PorteiroUiData(
    val id: String,
    val name: String,
    val cpf: String,
    val email: String
)

fun PorteiroInfo.toUi(): PorteiroUiData = PorteiroUiData(
    id = id,
    name = name,
    cpf = cpf,
    email = email
)
