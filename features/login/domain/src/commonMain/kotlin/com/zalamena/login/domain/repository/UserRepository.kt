package com.zalamena.login.domain.repository

import com.zalamena.login.domain.models.User
import com.zalamena.login.domain.models.UserRole

interface UserRepository {

    suspend fun createUser(
        name: String,
        cpf: String,
        email: String,
        role: UserRole
    ): Result<User>

    suspend fun getUsers(): Result<List<User>>

    suspend fun getUsersByCondominioId(condominioId: String): Result<List<User>>

    suspend fun deleteUser(userId: String): Result<Unit>

    suspend fun updateUserRole(userId: String, newRole: UserRole): Result<Unit>
}
