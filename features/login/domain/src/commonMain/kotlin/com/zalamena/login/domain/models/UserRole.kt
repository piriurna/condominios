package com.zalamena.login.domain.models

sealed class UserRole {
    data object Admin : UserRole()
    data class Porteiro(val condominioId: String) : UserRole()
    data class Morador(val pessoaId: String, val condominioId: String) : UserRole()
}
