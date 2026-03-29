package com.zalamena.login.data.repository

import com.zalamena.login.data.api.LoginApi
import com.zalamena.login.data.mapper.toDomain
import com.zalamena.login.domain.models.User
import com.zalamena.login.domain.models.UserRole
import com.zalamena.login.domain.repository.LoginException
import com.zalamena.login.domain.repository.LoginRepository

class LoginRepositoryImpl (
    private val loginApi: LoginApi,
    private val sessionRepository: SessionRepository
): LoginRepository {

    override suspend fun isLoggedIn(): Boolean = sessionRepository.isLoggedIn()

    override suspend fun getRole(): UserRole? {
        val roleStr = sessionRepository.getRole() ?: return null
        return when (roleStr.uppercase()) {
            "ADMIN" -> UserRole.Admin
            "PORTEIRO" -> {
                val condominioId = sessionRepository.getCondominioId() ?: ""
                UserRole.Porteiro(condominioId)
            }
            "MORADOR" -> {
                val condominioId = sessionRepository.getCondominioId() ?: ""
                val pessoaId = sessionRepository.getPessoaId() ?: ""
                UserRole.Morador(pessoaId = pessoaId, condominioId = condominioId)
            }
            else -> null
        }
    }

    override suspend fun logout() {
        sessionRepository.clearSession()
    }

    override suspend fun login(username: String, password: String): Result<User> {
        return try {
            val session = loginApi.login(username, password)

            val userResult = loginApi.getUser(session.userId)

            if(userResult == null) {
                throw LoginException.NonExistentUserException
            }

            sessionRepository.saveSession(session.token, session.expiresIn, userResult.role)

            if (userResult.condominioId != null) {
                sessionRepository.saveCondominioId(userResult.condominioId)
            }

            if (userResult.pessoaId != null) {
                sessionRepository.savePessoaId(userResult.pessoaId)
            }

            Result.success(userResult.toDomain())
        } catch (e: LoginException) {
            Result.failure(e)
        }
        catch (_: Exception) {
            Result.failure(LoginException.GenericErrorException("Login Failed"))
        }
    }
}
