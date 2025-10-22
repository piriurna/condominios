package com.zalamena.condominios.condominio.domain.moradores.repository

import com.zalamena.condominios.condominio.domain.moradores.models.ApartamentoWithMoradores
import com.zalamena.condominios.condominio.domain.moradores.models.Morador

interface MoradoresRepository {
    suspend fun addMorador(pessoa: String, apartamento: String): Result<Unit>

    suspend fun getMorador(id: String, apartamentoId: String): Result<Morador>

    suspend fun getAllMoradores(): Result<List<Morador>>

    suspend fun getAllMoradoresForApartamento(apartamentoId: String): Result<List<Morador>>

    suspend fun getApartamentoWithMoradores(apartamentoId: String): Result<ApartamentoWithMoradores>
}