package com.zalamena.condominios.condominio.domain.morador.repository

import com.zalamena.condominios.condominio.domain.morador.model.Morador

interface MoradoresRepository {
    suspend fun addMorador(pessoa: String, apartamento: String): Result<Unit>

    suspend fun getMorador(id: String, apartamentoId: String): Result<Morador>
}
