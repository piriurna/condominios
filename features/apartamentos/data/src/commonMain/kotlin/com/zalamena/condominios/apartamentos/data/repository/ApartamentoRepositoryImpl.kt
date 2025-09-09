package com.zalamena.condominios.apartamentos.data.repository

import com.zalamena.condominios.apartamentos.domain.models.Apartamento
import com.zalamena.condominios.apartamentos.domain.repository.ApartamentosRepository

class ApartamentoRepositoryImpl(

): ApartamentosRepository {
    override suspend fun getApartamento(apartamentoId: String): Result<Apartamento> {
        TODO("Not yet implemented")
    }

    override suspend fun addApartamento(apartamento: Apartamento): Result<Unit> {
        TODO("Not yet implemented")
    }
}