package com.zalamena.login.data.repository

import com.zalamena.login.domain.repository.LoginRepository

class LoginRepositoryImpl: LoginRepository {
    override suspend fun login(username: String, password: String): String {
        return "token"

    }
}