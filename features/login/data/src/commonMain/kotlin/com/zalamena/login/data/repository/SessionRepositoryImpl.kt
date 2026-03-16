package com.zalamena.login.data.repository

class SessionRepositoryImpl : SessionRepository {

    private var token: String? = null
    private var expiresIn: Long = 0L

    override suspend fun saveSession(authToken: String, expiresIn: Long) {
        token = authToken
        this.expiresIn = expiresIn
    }

    override suspend fun isLoggedIn(): Boolean = token != null
}
