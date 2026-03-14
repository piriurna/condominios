package com.zalamena.condominios.condominio.domain.condominio.repository

import com.zalamena.condominios.condominio.domain.condominio.models.Condominio

interface CondominioRepository {


    suspend fun getCondominio(condominioId: String): Result<Condominio>

    suspend fun addCondominio(condominio: Condominio): Result<Unit>
}