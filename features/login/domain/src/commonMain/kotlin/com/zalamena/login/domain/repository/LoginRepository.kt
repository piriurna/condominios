package com.zalamena.login.domain.repository

import com.zalamena.login.domain.repository.LoginException

interface LoginRepository {

    @Throws(LoginException::class)
    suspend fun login(username: String, password: String): String
}