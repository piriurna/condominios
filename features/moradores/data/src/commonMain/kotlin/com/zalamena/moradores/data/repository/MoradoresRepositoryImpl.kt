package com.zalamena.moradores.data.repository

import com.zalamena.moradores.data.dao.MoradoresDao
import com.zalamena.moradores.data.mapper.MoradorMapper
import com.zalamena.moradores.data.utils.now
import com.zalamena.moradores.domain.models.Morador
import com.zalamena.moradores.domain.repository.MoradoresRepository
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.plus

class MoradoresRepositoryImpl(
    val moradoresDao: MoradoresDao,
    val moradorMapper: MoradorMapper
): MoradoresRepository {

    override fun addMorador(morador: Morador): Result<Unit> {
        return runCatching {
            with(moradorMapper) {
                Result.success(moradoresDao.addMorador(morador.toEntity()))
            }
        }
    }

    override fun getMorador(cpf: String): Result<Morador?> {
        return runCatching {
            with(moradorMapper) {
                moradoresDao.getMorador(cpf).toDomain()
            }
        }
    }

    override fun getAllMoradores(): Result<List<Morador>> {
        return runCatching {
            with(moradorMapper) {
                moradoresDao.getAllMoradores().map { it.toDomain() }
            }
        }
    }

}