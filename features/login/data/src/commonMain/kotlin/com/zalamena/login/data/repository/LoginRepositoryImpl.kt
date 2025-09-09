package com.zalamena.login.data.repository

import com.zalamena.login.data.api.LoginApi
import com.zalamena.login.data.mapper.toDomain
import com.zalamena.login.domain.models.User
import com.zalamena.login.domain.repository.LoginException
import com.zalamena.login.domain.repository.LoginRepository

class LoginRepositoryImpl (
    private val loginApi: LoginApi,
    private val sessionRepository: SessionRepository
): LoginRepository {
    override suspend fun login(username: String, password: String): Result<User> {
        return try {
            if(username.isEmpty() || password.isEmpty()) {
                throw LoginException.InvalidCredentialsException
            }
            val session = loginApi.login(username, password)

            val userResult =  loginApi.getUser(session.userId)

            if(userResult == null) {
                throw LoginException.NonExistentUserException
            }

            sessionRepository.saveSession(session.token, session.expiresIn)
            Result.success(userResult.toDomain())
        } catch (e: LoginException) {
            Result.failure(e)
        }
        catch (_: Exception) {
            Result.failure(LoginException.GenericErrorException("Login Failed"))
        }
    }
}