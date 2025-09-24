package com.zalamena.condominios.apartamentos.domain.models

sealed class ApartamentoException(message: String): Exception(message) {



    object NoApartmentFoundException: ApartamentoException("Nenhum apartamento encontrado")


    object DuplicatedApartmentException: ApartamentoException("Apartamento Já cadastrado")
}