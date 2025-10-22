package com.zalamena.condominios.condominio.domain.apartamento.models

sealed class ApartamentoException(message: String): Exception(message) {



    object NoApartmentFoundException: ApartamentoException("Nenhum apartamento encontrado")


    object DuplicatedApartmentException: ApartamentoException("Apartamento Já cadastrado")
}