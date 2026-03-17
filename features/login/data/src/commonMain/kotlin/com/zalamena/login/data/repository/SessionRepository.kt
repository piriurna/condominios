package com.zalamena.login.data.repository

interface SessionRepository {
    suspend fun saveSession(authToken: String, expiresIn: Long, role: String)
    suspend fun isLoggedIn(): Boolean
    suspend fun saveRole(role: String)
    suspend fun getRole(): String?
    suspend fun saveCondominioId(condominioId: String)
    suspend fun getCondominioId(): String?
    suspend fun clearSession()
}
