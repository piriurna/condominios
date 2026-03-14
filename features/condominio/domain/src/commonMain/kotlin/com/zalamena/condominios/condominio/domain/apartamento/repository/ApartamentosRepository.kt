package com.zalamena.condominios.condominio.domain.apartamento.repository

import com.zalamena.condominios.condominio.domain.apartamento.models.Apartamento


interface ApartamentosRepository {
    suspend fun getApartamento(apartamentoId: String): Result<Apartamento>

    suspend fun getApartamentos(): Result<List<Apartamento>>

    suspend fun addApartamento(condominioId: String, apartamento: Apartamento): Result<Unit>

    suspend fun createApartamentoId(numeroApartamento: String): Result<String>
}