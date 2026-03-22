package com.zalamena.condominios.condominio.data.moradores.repository

import com.zalamena.condominios.condominio.data.moradores.dao.MoradoresDao
import com.zalamena.condominios.condominio.data.moradores.entities.MoradorEntity
import com.zalamena.condominios.condominio.data.moradores.mapper.toDomain
import com.zalamena.condominios.condominio.domain.morador.model.Morador
import com.zalamena.condominios.condominio.domain.morador.model.MoradorException
import com.zalamena.condominios.condominio.domain.morador.model.MoradorTipo
import com.zalamena.condominios.condominio.domain.morador.repository.MoradoresRepository


class MoradoresRepositoryImpl(
    val moradoresDao: MoradoresDao
): MoradoresRepository {
    override suspend fun addMorador(
        pessoaId: String,
        apartamentoId: String,
        tipo: MoradorTipo
    ): Result<Unit> {
        return runCatching {
            val morador = MoradorEntity(
                pessoaId = pessoaId,
                apartamentoId = apartamentoId,
                tipo = tipo.name
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
                ?: throw MoradorException.MoradorNotFoundException
        }
    }

    override suspend fun getMoradoresForApartamento(apartamentoId: String): Result<List<Morador>> {
        return runCatching {
            moradoresDao.getAllMoradoresForApartamento(apartamentoId).map { it.toDomain() }
        }
    }

    override suspend fun getMoradoresForCondominio(condominioId: String): Result<List<Morador>> {
        return runCatching {
            moradoresDao.getAllMoradoresForCondominio(condominioId).map { it.toDomain() }
        }
    }

    override suspend fun getMoradoresForPessoa(pessoaId: String): Result<List<Morador>> {
        return runCatching {
            moradoresDao.getAllMoradoresForPessoa(pessoaId).map { it.toDomain() }
        }
    }

    override suspend fun removeMorador(pessoaId: String, apartamentoId: String): Result<Unit> {
        return runCatching {
            moradoresDao.deleteMorador(pessoaId, apartamentoId)
        }
    }

    override suspend fun updateMoradorTipo(pessoaId: String, apartamentoId: String, newTipo: MoradorTipo): Result<Unit> {
        return runCatching {
            moradoresDao.updateMoradorTipo(pessoaId, apartamentoId, newTipo.name)
        }
    }
}