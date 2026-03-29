package com.zalamena.login.domain.repository

/**
 * Cross-module interface to validate a pessoa is a morador and retrieve their condominioId.
 * Implemented in DI using the condominio module's repository.
 */
fun interface MoradorValidator {
    /**
     * Returns the condominioId if the pessoa is a morador, or null if not found.
     */
    suspend fun getCondominioIdForMorador(pessoaId: String): String?
}
