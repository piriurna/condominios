package com.zalamena.login.data.api

import com.zalamena.login.data.models.LoginSessionDto
import com.zalamena.login.data.models.UserDto
import com.zalamena.login.domain.repository.LoginException

class FakeLoginApi : LoginApi {

    override suspend fun login(username: String, password: String): LoginSessionDto {
        if (username == "admin" && password == "admin") {
            return LoginSessionDto(token = "fake-token-admin", userId = "user-1", expiresIn = 86400L)
        }
        throw LoginException.InvalidCredentialsException
    }

    override suspend fun getUser(userId: String): UserDto? {
        return when (userId) {
            "user-1" -> UserDto(username = "Administrador", cpf = "00000000000", email = "admin@condominio.com")
            else -> null
        }
    }
}
