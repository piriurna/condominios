package com.zalamena.condominios.condominio.data.apartamento.repository

import com.zalamena.condominios.condominio.data.apartamento.dao.ApartamentoDao
import com.zalamena.condominios.condominio.data.apartamento.mapper.toDomain
import com.zalamena.condominios.condominio.data.apartamento.mapper.toEntity
import com.zalamena.condominios.condominio.domain.apartamento.models.Apartamento
import com.zalamena.condominios.condominio.domain.apartamento.models.ApartamentoException
import com.zalamena.condominios.condominio.domain.apartamento.repository.ApartamentosRepository

class ApartamentoRepositoryImpl(
    private val apartamentoDao: ApartamentoDao
): ApartamentosRepository {
    override suspend fun getApartamento(apartamentoId: String): Result<Apartamento> {
        return runCatching {
            apartamentoDao.getApartamento(apartamentoId)?.toDomain() ?: throw ApartamentoException.NoApartmentFoundException
        }
    }

    override suspend fun getApartamentos(): Result<List<Apartamento>> {
        return runCatching {
            apartamentoDao.getApartamentos().map { it.toDomain() }
        }
    }

    override suspend fun addApartamento(condominioId: String, apartamento: Apartamento): Result<Unit> {
        return runCatching {
            apartamentoDao.addApartamento(apartamento.toEntity(condominioId))
        }
    }

    override suspend fun createApartamentoId(numeroApartamento: String): Result<String> {
        return runCatching {
            "${apartamentoDao.getApartamentos().size}-${numeroApartamento}"
        }
    }
}