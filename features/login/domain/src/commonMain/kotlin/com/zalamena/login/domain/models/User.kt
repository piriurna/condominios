package com.zalamena.login.domain.models

data class User(
    val id: String,
    val name: String,
    val cpf: String,
    val email: String,
    val role: UserRole
)
