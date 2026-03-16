package com.zalamena.login.data.api

import com.zalamena.login.data.models.LoginSessionDto
import com.zalamena.login.data.models.UserDto
import com.zalamena.login.domain.repository.LoginException

class FakeLoginApi : LoginApi {

    override suspend fun login(username: String, password: String): LoginSessionDto {
        return when {
            username == "admin" && password == "admin" ->
                LoginSessionDto(token = "fake-token-admin", userId = "user-1", expiresIn = 86400L)
            username == "porteiro" && password == "porteiro" ->
                LoginSessionDto(token = "fake-token-porteiro", userId = "user-2", expiresIn = 86400L)
            else -> throw LoginException.InvalidCredentialsException
        }
    }

    override suspend fun getUser(userId: String): UserDto? {
        return when (userId) {
            "user-1" -> UserDto(username = "Administrador", cpf = "00000000000", email = "admin@condominio.com", role = "ADMIN")
            "user-2" -> UserDto(username = "Porteiro", cpf = "11111111111", email = "porteiro@condominio.com", role = "PORTEIRO")
            else -> null
        }
    }
}
