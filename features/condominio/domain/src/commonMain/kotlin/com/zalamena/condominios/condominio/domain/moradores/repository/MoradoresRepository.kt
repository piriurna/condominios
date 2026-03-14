package com.zalamena.condominios.condominio.domain.moradores.repository

import com.zalamena.condominios.condominio.domain.moradores.models.Morador

interface MoradoresRepository {
    suspend fun addMorador(pessoa: String, apartamento: String): Result<Unit>

    suspend fun getMorador(id: String, apartamentoId: String): Result<Morador>
}