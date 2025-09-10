package com.zalamena.moradores.domain.repository

import com.zalamena.condominios.apartamentos.domain.models.Apartamento
import com.zalamena.condominios.individuo.domain.models.Individuo
import com.zalamena.moradores.domain.models.ApartamentoWithMoradores
import com.zalamena.moradores.domain.models.Morador

interface MoradoresRepository {
    suspend fun addMorador(individuo: Individuo, apartamento: Apartamento): Result<Unit>

    suspend fun getMorador(cpf: String, apartamentoId: String): Result<Morador>

    suspend fun getAllMoradores(): Result<List<Morador>>

    suspend fun getAllMoradoresForApartamento(apartamentoId: String): Result<List<Morador>>

    suspend fun getApartamentoWithMoradores(apartamentoId: String): Result<ApartamentoWithMoradores>
}