package com.zalamena.moradores.data.repository

import com.zalamena.condominios.apartamentos.domain.repository.ApartamentosRepository
import com.zalamena.condominios.pessoa.data.mapper.toDomain
import com.zalamena.moradores.data.dao.MoradoresDao
import com.zalamena.moradores.data.entities.MoradorEntity
import com.zalamena.moradores.data.mapper.MoradorMapper
import com.zalamena.moradores.domain.models.ApartamentoWithMoradores
import com.zalamena.moradores.domain.models.Morador
import com.zalamena.moradores.domain.models.MoradorException
import com.zalamena.moradores.domain.repository.MoradoresRepository

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