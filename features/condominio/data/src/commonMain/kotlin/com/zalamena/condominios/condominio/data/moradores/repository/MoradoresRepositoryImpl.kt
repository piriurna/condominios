package com.zalamena.condominios.condominio.data.moradores.repository

import com.zalamena.condominios.condominio.data.moradores.dao.MoradoresDao
import com.zalamena.condominios.condominio.data.moradores.entities.MoradorEntity
import com.zalamena.condominios.condominio.data.moradores.mapper.toDomain
import com.zalamena.condominios.condominio.domain.moradores.models.Morador
import com.zalamena.condominios.condominio.domain.moradores.models.MoradorException
import com.zalamena.condominios.condominio.domain.moradores.repository.MoradoresRepository


class MoradoresRepositoryImpl(
    val moradoresDao: MoradoresDao
): MoradoresRepository {
    override suspend fun addMorador(
        pessoa: String,
        apartamento: String
    ): Result<Unit> {
        return runCatching {
            val morador = MoradorEntity(
                pessoaId = pessoa,
                apartamentoId = apartamento,
            )
            moradoresDao.addMorador(morador)
        }
    }

    override suspend fun getMorador(
        id: String,
        apartamentoId: String
    ): Result<Morador> {
        return runCatching {
            moradoresDao.getMorador(id, apartamentoId)?.toDomain()
                ?:throw MoradorException.MoradorNotFoundException
        }
    }
}