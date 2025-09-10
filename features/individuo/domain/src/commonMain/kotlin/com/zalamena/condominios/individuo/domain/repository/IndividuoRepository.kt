package com.zalamena.condominios.individuo.domain.repository

import com.zalamena.condominios.individuo.domain.models.Individuo

interface IndividuoRepository {
    suspend fun addIndividuo(individuo: Individuo): Result<Individuo>

    suspend fun getIndividuo(cpf: String): Result<Individuo>

    suspend fun getAllIndividos(): Result<List<Individuo>>
}