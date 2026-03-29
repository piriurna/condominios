package com.zalamena.login.data.api

import com.zalamena.login.data.models.LoginSessionDto
import com.zalamena.login.data.models.UserDto
import com.zalamena.login.domain.models.UserRole
import com.zalamena.login.domain.repository.LoginException
import com.zalamena.login.domain.repository.UserRepository

class FakeLoginApi(
    private val userRepository: UserRepository
) : LoginApi {

    override suspend fun login(username: String, password: String): LoginSessionDto {
        if (username == "admin" && password == "admin") {
            return LoginSessionDto(token = "fake-token-admin", userId = "user-1", expiresIn = 86400L)
        }

        if (username == "porteiro" && password == "porteiro") {
            val porteiro = userRepository.getUsers().getOrNull()
                ?.firstOrNull { it.role is UserRole.Porteiro }
                ?: throw LoginException.InvalidCredentialsException
            return LoginSessionDto(
                token = "fake-token-${porteiro.id}",
                userId = porteiro.id,
                expiresIn = 86400L
            )
        }

        val result = userRepository.authenticate(username, password)
        if (result.isSuccess) {
            val user = result.getOrThrow()
            return LoginSessionDto(
                token = "fake-token-${user.id}",
                userId = user.id,
                expiresIn = 86400L
            )
        }

        throw LoginException.InvalidCredentialsException
    }

    override suspend fun getUser(userId: String): UserDto? {
        if (userId == "user-1") {
            return UserDto(username = "Administrador", cpf = "00000000000", email = "admin@condominio.com", role = "ADMIN")
        }

        val user = userRepository.getUsers().getOrNull()?.find { it.id == userId } ?: return null

        val condominioId = when (val role = user.role) {
            is UserRole.Porteiro -> role.condominioId
            is UserRole.Morador -> role.condominioId
            is UserRole.Admin -> null
        }

        val pessoaId = when (val role = user.role) {
            is UserRole.Morador -> role.pessoaId
            else -> null
        }

        return UserDto(
            username = user.name,
            cpf = user.cpf,
            email = user.email,
            role = when (user.role) {
                is UserRole.Admin -> "ADMIN"
                is UserRole.Porteiro -> "PORTEIRO"
                is UserRole.Morador -> "MORADOR"
            },
            condominioId = condominioId,
            pessoaId = pessoaId
        )
    }
}
