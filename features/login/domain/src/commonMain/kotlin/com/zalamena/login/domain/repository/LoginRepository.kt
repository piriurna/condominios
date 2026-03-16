package com.zalamena.login.domain.repository

import com.zalamena.login.domain.models.User
import com.zalamena.login.domain.models.UserRole

interface LoginRepository {

    suspend fun login(username: String, password: String): Result<User>

    suspend fun isLoggedIn(): Boolean

    suspend fun getRole(): UserRole?
}