package com.zalamena.condominios.condominio.data.condominio.repository

import com.zalamena.condominios.condominio.data.condominio.dao.CondominioDao
import com.zalamena.condominios.condominio.data.condominio.mapper.toCondominio
import com.zalamena.condominios.condominio.data.condominio.mapper.toEntity
import com.zalamena.condominios.condominio.domain.condominio.models.Condominio
import com.zalamena.condominios.condominio.domain.condominio.repository.CondominioRepository

class CondominioRepositoryImpl constructor(
    private val condominioDao: CondominioDao
): CondominioRepository {
    override suspend fun getCondominios(): Result<List<Condominio>> = runCatching {
        condominioDao.getCondominios().map { it.toCondominio() }
    }

    override suspend fun getCondominio(condominioId: String): Result<Condominio> {
        return runCatching {
            condominioDao.getCondominio(condominioId)?.toCondominio()?: throw Exception("Condominio not found")
        }
    }

    override suspend fun addCondominio(condominio: Condominio): Result<Unit> {
        return runCatching {
            val endereco = condominio.endereco.toEntity().let { enderecoEntity ->
                val existingEndereco = condominioDao.getEndereco(enderecoEntity.id)
                if (existingEndereco == null) {
                    condominioDao.insertEndereco(enderecoEntity)
                    enderecoEntity
                } else {
                    existingEndereco
                }
            }
            condominioDao.insertCondominio(condominio.toEntity(endereco.id))
        }
    }
}