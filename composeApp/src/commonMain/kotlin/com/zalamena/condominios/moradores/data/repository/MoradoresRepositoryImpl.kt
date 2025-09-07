package com.zalamena.condominios.moradores.data.repository

import com.zalamena.condominios.common.utils.now
import com.zalamena.condominios.moradores.data.dao.MoradoresDao
import com.zalamena.condominios.moradores.data.mapper.MoradorMapper
import com.zalamena.condominios.moradores.domain.models.Morador
import com.zalamena.condominios.moradores.domain.repository.MoradoresRepository
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.plus

class MoradoresRepositoryImpl(
    val moradoresDao: MoradoresDao,
    val moradorMapper: MoradorMapper
): MoradoresRepository {

    override fun loginMorador(morador: Morador) {
        with(moradorMapper) {
            val expiryDate = LocalDateTime.now().date.plus(DatePeriod(days = 1))
            moradoresDao.setLoggedInMorador(morador.cpf, expiryDate.toEpochDays())
        }
    }

    override fun getMorador(morador: Morador): Morador? {
        with(moradorMapper) {
            return moradoresDao.getAllMoradores().firstOrNull { it.cpf == morador.cpf }?.toDomain()
        }
    }

    override fun getCurrentLoggedMorador(): Morador? {
        with(moradorMapper) {
            return moradoresDao.getLoggedInMorador()?.toDomain()
        }
    }

    override fun addMorador(morador: Morador) {
        with(moradorMapper) {
            return moradoresDao.addMorador(morador.toEntity())
        }
    }

    override fun getAllMoradores(): List<Morador> {
        with(moradorMapper) {
            return moradoresDao.getAllMoradores().map { it.toDomain() }
        }

    }

}