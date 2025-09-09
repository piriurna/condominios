package com.zalamena.login.data.mapper

import com.zalamena.login.data.models.UserDto
import com.zalamena.login.domain.models.User


fun UserDto.toDomain(): User {
    return User(
        name = username,
        cpf = cpf,
        email = email
    )
}