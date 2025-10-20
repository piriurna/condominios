package com.zalamena.condominios.condominio.data.moradores.repository

import com.zalamena.condominios.condominio.data.moradores.dao.MoradoresDao
import com.zalamena.condominios.condominio.data.moradores.entities.MoradorEntity
import com.zalamena.condominios.condominio.data.moradores.mapper.MoradorMapper
import com.zalamena.condominios.condominio.domain.apartamento.repository.ApartamentosRepository
import com.zalamena.condominios.condominio.domain.moradores.models.ApartamentoWithMoradores
import com.zalamena.condominios.condominio.domain.moradores.models.Morador
import com.zalamena.condominios.condominio.domain.moradores.models.MoradorException
import com.zalamena.condominios.condominio.domain.moradores.repository.MoradoresRepository
import com.zalamena.condominios.pessoa.data.mapper.toDomain


class MoradoresRepositoryImpl(
    val moradoresDao: MoradoresDao,
    val apartamentoRepository: ApartamentosRepository,
    val moradorMapper: MoradorMapper
): MoradoresRepository {
    override suspend fun addMorador(
        pessoa: String,
        apartamento: String
    ): Result<Unit> {
        return runCatching {
            with(moradorMapper) {
                val morador = MoradorEntity(
                    pessoaId = pessoa,
                    apartamentoId = apartamento,
                )
                moradoresDao.addMorador(morador)
            }
        }
    }

    override suspend fun getMorador(
        id: String,
        apartamentoId: String
    ): Result<Morador> {
        return runCatching {
            with(moradorMapper) {
                moradoresDao.getMorador(id, apartamentoId)?.toDomain()
                    ?:throw MoradorException.MoradorNotFoundException
            }
        }
    }

    override suspend fun getAllMoradores(): Result<List<Morador>> {
        return runCatching {
            with(moradorMapper) {
                moradoresDao.getAllMoradores().map { it.toDomain() }
            }
        }
    }

    override suspend fun getAllMoradoresForApartamento(apartamentoId: String): Result<List<Morador>> {
        return runCatching {
            with(moradorMapper) {
                moradoresDao
                    .getAllMoradoresForApartamento(apartamentoId)
                    .map { it.toDomain() }
            }
        }
    }

    override suspend fun getApartamentoWithMoradores(apartamentoId: String): Result<ApartamentoWithMoradores> {
        return runCatching {
            with(moradorMapper) {
                val apartamento = apartamentoRepository
                    .getApartamento(apartamentoId)
                    .getOrThrow()

                val pessoas = moradoresDao
                    .getAllMoradoresForApartamento(apartamentoId)


                return@runCatching ApartamentoWithMoradores(
                    apartamento = apartamento,
                    moradores = pessoas.map { it.pessoa.toDomain() }
                )
            }
        }
    }

}