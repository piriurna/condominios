package com.zalamena.condominios.individuo.data.repository

import com.zalamena.condominios.individuo.data.dao.IndividuoDao
import com.zalamena.condominios.individuo.domain.models.Individuo
import com.zalamena.condominios.individuo.domain.repository.IndividuoRepository

class IndividuoRepositoryImpl constructor(
    private val individuoDao: IndividuoDao
): IndividuoRepository {
    override suspend fun addIndividuo(individuo: Individuo): Result<Individuo> {
        TODO("Not yet implemented")
    }

    override suspend fun getIndividuo(cpf: String): Result<Individuo> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllIndividos(): Result<List<Individuo>> {
        TODO("Not yet implemented")
    }
}