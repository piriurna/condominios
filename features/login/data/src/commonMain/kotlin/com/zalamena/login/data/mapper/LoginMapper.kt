package com.zalamena.login.data.mapper

import com.zalamena.login.data.models.UserDto
import com.zalamena.login.domain.models.User
import com.zalamena.login.domain.models.UserRole


fun UserDto.toDomain(): User {
    return User(
        name = username,
        cpf = cpf,
        email = email,
        role = UserRole.valueOf(role),
        condominioId = null
    )
}