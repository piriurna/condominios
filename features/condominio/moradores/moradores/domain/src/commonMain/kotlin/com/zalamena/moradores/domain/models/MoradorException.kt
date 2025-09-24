package com.zalamena.moradores.domain.models

sealed class MoradorException(override val message: String) : Exception(message) {

    object MoradorNotFoundException: MoradorException("Morador Não Encontrado")
    object DuplicateMoradorException: MoradorException("Morador já existe")
}