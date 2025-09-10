package com.zalamena.condominios.individuo.domain.models

sealed class IndividuoException(override val message: String) : Exception(message) {

    object IndividuoNotFoundException: IndividuoException("Individuo Não Encontrado")
    object DuplicateIndividuoException: IndividuoException("Individuo já existe")
}