package com.zalamena.login.domain.repository

fun interface CondominioValidator {
    suspend fun exists(condominioId: String): Boolean
}
