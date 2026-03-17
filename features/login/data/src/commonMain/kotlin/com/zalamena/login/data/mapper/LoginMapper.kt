package com.zalamena.login.data.mapper

import com.zalamena.login.data.models.UserDto
import com.zalamena.login.domain.models.User
import com.zalamena.login.domain.models.UserRole


fun UserDto.toDomain(): User {
    val userRole = when (role.uppercase()) {
        "ADMIN" -> UserRole.Admin
        "PORTEIRO" -> UserRole.Porteiro(condominioId = condominioId ?: "")
        else -> throw IllegalArgumentException("Unknown role: $role")
    }
    return User(
        name = username,
        cpf = cpf,
        email = email,
        role = userRole
    )
}
