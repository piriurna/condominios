package com.zalamena.condominios.condominio.domain.morador.repository

/**
 * Cross-module interface to create a user account for a morador.
 * Returns Result.success(email) on success, or Result.failure with the error.
 */
fun interface MoradorAccountCreator {
    suspend fun createAccount(pessoaId: String, email: String, password: String): Result<String>
}
