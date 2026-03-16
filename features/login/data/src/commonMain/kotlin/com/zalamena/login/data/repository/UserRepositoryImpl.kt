package com.zalamena.login.data.repository

import com.zalamena.login.domain.models.User
import com.zalamena.login.domain.models.UserRole
import com.zalamena.login.domain.repository.UserRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class UserRepositoryImpl : UserRepository {

    private val users = mutableListOf<User>()

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun createUser(
        name: String,
        cpf: String,
        email: String,
        role: UserRole,
        condominioId: String?
    ): Result<User> {
        return try {
            val user = User(
                name = name,
                cpf = cpf,
                email = email,
                role = role,
                condominioId = condominioId
            )
            users.add(user)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUsers(): Result<List<User>> {
        return Result.success(users.toList())
    }
}
