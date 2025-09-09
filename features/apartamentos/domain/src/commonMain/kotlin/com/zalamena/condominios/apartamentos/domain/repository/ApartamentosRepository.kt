package com.zalamena.condominios.apartamentos.domain.repository

import com.zalamena.condominios.apartamentos.domain.models.Apartamento

interface ApartamentosRepository {
    suspend fun getApartamento(apartamentoId: String): Result<Apartamento>


    suspend fun addApartamento(apartamento: Apartamento): Result<Unit>
}