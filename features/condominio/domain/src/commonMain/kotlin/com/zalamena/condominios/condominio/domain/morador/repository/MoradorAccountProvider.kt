package com.zalamena.condominios.condominio.domain.morador.repository

/**
 * Cross-module interface to check if a morador has a user account.
 * Returns the account email if one exists, or null if the morador has no account.
 */
fun interface MoradorAccountProvider {
    suspend fun getAccountEmailForPessoa(pessoaId: String): String?
}
