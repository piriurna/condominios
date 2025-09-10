package com.zalamena.condominios.apartamentos.data.repository

import com.zalamena.condominios.apartamentos.data.dao.ApartamentoDao
import com.zalamena.condominios.apartamentos.data.mapper.toDomain
import com.zalamena.condominios.apartamentos.data.mapper.toEntity
import com.zalamena.condominios.apartamentos.domain.models.Apartamento
import com.zalamena.condominios.apartamentos.domain.models.ApartamentoException
import com.zalamena.condominios.apartamentos.domain.repository.ApartamentosRepository

class ApartamentoRepositoryImpl(
    private val apartamentoDao: ApartamentoDao
): ApartamentosRepository {
    override suspend fun getApartamento(apartamentoId: String): Result<Apartamento> {
        return runCatching {
            apartamentoDao.getApartamento(apartamentoId)?.toDomain() ?: throw ApartamentoException.NoApartmentFoundException
        }
    }

    override suspend fun addApartamento(apartamento: Apartamento): Result<Unit> {
        return runCatching {
            apartamentoDao.addApartamento(apartamento.toEntity())
        }
    }
}