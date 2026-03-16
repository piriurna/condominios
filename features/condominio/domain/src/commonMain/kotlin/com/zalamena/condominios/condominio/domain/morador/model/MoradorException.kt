package com.zalamena.condominios.condominio.domain.morador.model

sealed class MoradorException(override val message: String) : Exception(message) {

    object MoradorNotFoundException : MoradorException("Morador Não Encontrado")
    object DuplicateMoradorException : MoradorException("Morador já existe")
    object MaxProprietariosExceededException : MoradorException("Limite de 2 proprietários por apartamento atingido")
}
