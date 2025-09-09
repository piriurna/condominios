package com.zalamena.login.domain.repository

import com.zalamena.login.domain.models.User

interface LoginRepository {

    suspend fun login(username: String, password: String): Result<User>
}