package com.zalamena.condominios.individuo.data.repository

import com.zalamena.condominios.individuo.data.dao.IndividuoDao
import com.zalamena.condominios.individuo.data.mapper.toDomain
import com.zalamena.condominios.individuo.data.mapper.toEntity
import com.zalamena.condominios.individuo.domain.models.Individuo
import com.zalamena.condominios.individuo.domain.models.IndividuoException
import com.zalamena.condominios.individuo.domain.repository.IndividuoRepository

class IndividuoRepositoryImpl(
    private val individuoDao: IndividuoDao
): IndividuoRepository {
    override suspend fun addIndividuo(individuo: Individuo): Result<Individuo> {
        return runCatching {
            individuoDao.addIndividuo(individuo.toEntity())

            return@runCatching individuo
        }
    }

    override suspend fun getIndividuo(cpf: String): Result<Individuo> {
        return runCatching {
            return@runCatching individuoDao.getIndividuo(cpf)?.toDomain()
                ?: throw IndividuoException.IndividuoNotFoundException
        }
    }

    override suspend fun getAllIndividos(): Result<List<Individuo>> {
        return runCatching {
            return@runCatching individuoDao.getAllIndividos().map { it.toDomain() }
        }
    }
}