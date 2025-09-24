package com.zalamena.condominios.apartamentos.domain.repository

import com.zalamena.condominios.apartamentos.domain.models.Apartamento

interface ApartamentosRepository {
    suspend fun getApartamento(apartamentoId: String): Result<Apartamento>

    suspend fun getApartamentos(): Result<List<Apartamento>>

    suspend fun addApartamento(apartamento: Apartamento): Result<Unit>

    suspend fun createApartamentoId(numeroApartamento: String): Result<String>
}