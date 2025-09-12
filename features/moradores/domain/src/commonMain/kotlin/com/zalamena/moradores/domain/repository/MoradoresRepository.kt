package com.zalamena.moradores.domain.repository

import com.zalamena.moradores.domain.models.ApartamentoWithMoradores
import com.zalamena.moradores.domain.models.Morador

interface MoradoresRepository {
    suspend fun addMorador(pessoa: String, apartamento: String): Result<Unit>

    suspend fun getMorador(id: String, apartamentoId: String): Result<Morador>

    suspend fun getAllMoradores(): Result<List<Morador>>

    suspend fun getAllMoradoresForApartamento(apartamentoId: String): Result<List<Morador>>

    suspend fun getApartamentoWithMoradores(apartamentoId: String): Result<ApartamentoWithMoradores>
}