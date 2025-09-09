package com.zalamena.login.data.repository

interface SessionRepository {
    suspend fun saveSession(authToken: String, expiresIn: Long)
}