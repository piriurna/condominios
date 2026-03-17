package com.zalamena.login.data.repository

class SessionRepositoryImpl : SessionRepository {

    private var token: String? = null
    private var expiresIn: Long = 0L
    private var role: String? = null
    private var condominioId: String? = null

    override suspend fun saveSession(authToken: String, expiresIn: Long, role: String) {
        token = authToken
        this.expiresIn = expiresIn
        this.role = role
    }

    override suspend fun isLoggedIn(): Boolean = token != null

    override suspend fun saveRole(role: String) {
        this.role = role
    }

    override suspend fun getRole(): String? = role

    override suspend fun saveCondominioId(condominioId: String) {
        this.condominioId = condominioId
    }

    override suspend fun getCondominioId(): String? = condominioId

    override suspend fun clearSession() {
        token = null
        expiresIn = 0L
        role = null
        condominioId = null
    }
}
