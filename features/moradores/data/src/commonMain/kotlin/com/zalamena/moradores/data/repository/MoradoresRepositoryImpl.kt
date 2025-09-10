package com.zalamena.moradores.data.repository

import com.zalamena.condominios.apartamentos.data.dao.ApartamentoDao
import com.zalamena.condominios.apartamentos.data.mapper.toDomain
import com.zalamena.condominios.apartamentos.data.mapper.toEntity
import com.zalamena.condominios.apartamentos.domain.models.Apartamento
import com.zalamena.condominios.apartamentos.domain.repository.ApartamentosRepository
import com.zalamena.condominios.individuo.data.mapper.toDomain
import com.zalamena.condominios.individuo.data.mapper.toEntity
import com.zalamena.condominios.individuo.domain.models.Individuo
import com.zalamena.condominios.individuo.domain.repository.IndividuoRepository
import com.zalamena.moradores.data.dao.MoradoresDao
import com.zalamena.moradores.data.entities.MoradorEntity
import com.zalamena.moradores.data.mapper.MoradorMapper
import com.zalamena.moradores.data.utils.now
import com.zalamena.moradores.domain.models.ApartamentoWithMoradores
import com.zalamena.moradores.domain.models.Morador
import com.zalamena.moradores.domain.models.MoradorException
import com.zalamena.moradores.domain.repository.MoradoresRepository
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.plus

class MoradoresRepositoryImpl(
    val moradoresDao: MoradoresDao,
    val apartamentoRepository: ApartamentosRepository,
    val moradorMapper: MoradorMapper
): MoradoresRepository {
    override suspend fun addMorador(
        individuo: Individuo,
        apartamento: Apartamento
    ): Result<Unit> {
        return runCatching {
            with(moradorMapper) {
                val morador = MoradorEntity(
                    individuoId = individuo.id,
                    apartamentoId = apartamento.id,
                )
                Result.success(moradoresDao.addMorador(morador))
            }
        }
    }

    override suspend fun getMorador(
        cpf: String,
        apartamentoId: String
    ): Result<Morador> {
        return runCatching {
            with(moradorMapper) {
                moradoresDao.getMorador(cpf, apartamentoId)?.toDomain()
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

                val individuos = moradoresDao
                    .getAllMoradoresForApartamento(apartamentoId)


                return@runCatching ApartamentoWithMoradores(
                    apartamento = apartamento,
                    moradores = individuos.map { it.individuo.toDomain() }
                )
            }
        }
    }

}