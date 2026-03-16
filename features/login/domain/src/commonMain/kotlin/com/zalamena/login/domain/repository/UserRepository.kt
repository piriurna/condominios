package com.zalamena.login.domain.repository

import com.zalamena.login.domain.models.User
import com.zalamena.login.domain.models.UserRole

interface UserRepository {

    suspend fun createUser(
        name: String,
        cpf: String,
        email: String,
        role: UserRole,
        condominioId: String?
    ): Result<User>

    suspend fun getUsers(): Result<List<User>>
}
