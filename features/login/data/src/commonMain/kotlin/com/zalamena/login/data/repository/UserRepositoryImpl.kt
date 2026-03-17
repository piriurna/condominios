package com.zalamena.login.data.repository

import com.zalamena.login.domain.models.User
import com.zalamena.login.domain.models.UserRole
import com.zalamena.login.domain.repository.UserRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class UserRepositoryImpl : UserRepository {

    private val users = mutableListOf<User>()
    private val passwords = mutableMapOf<String, String>()

    override suspend fun createUser(
        name: String,
        cpf: String,
        email: String,
        role: UserRole,
        password: String
    ): Result<User> {
        return try {
            val user = User(
                id = Uuid.random().toString(),
                name = name,
                cpf = cpf,
                email = email,
                role = role
            )
            users.add(user)
            passwords[user.id] = password
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun authenticate(email: String, password: String): Result<User> {
        val user = users.find { it.email == email }
            ?: return Result.failure(NoSuchElementException("User not found"))
        val storedPassword = passwords[user.id]
        return if (storedPassword == password) {
            Result.success(user)
        } else {
            Result.failure(IllegalArgumentException("Invalid password"))
        }
    }

    override suspend fun getUsers(): Result<List<User>> {
        return Result.success(users.toList())
    }

    override suspend fun getUsersByCondominioId(condominioId: String): Result<List<User>> {
        return try {
            val filtered = users.filter { user ->
                val role = user.role
                role is UserRole.Porteiro && role.condominioId == condominioId
            }
            Result.success(filtered)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteUser(userId: String): Result<Unit> {
        return try {
            val removed = users.removeAll { it.id == userId }
            if (removed) {
                Result.success(Unit)
            } else {
                Result.failure(NoSuchElementException("User not found: $userId"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateUserRole(userId: String, newRole: UserRole): Result<Unit> {
        return try {
            val index = users.indexOfFirst { it.id == userId }
            if (index == -1) {
                Result.failure(NoSuchElementException("User not found: $userId"))
            } else {
                users[index] = users[index].copy(role = newRole)
                Result.success(Unit)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
