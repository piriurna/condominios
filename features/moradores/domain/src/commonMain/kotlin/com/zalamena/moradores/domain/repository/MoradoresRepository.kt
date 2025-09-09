package com.zalamena.moradores.domain.repository

import com.zalamena.moradores.domain.models.Morador

interface MoradoresRepository {
    fun addMorador(morador: Morador): Result<Unit>

    fun getMorador(cpf: String): Result<Morador>

    fun getAllMoradores(): Result<List<Morador>>

    fun getAllMoradoresForApartamento(apartamentoId: String): Result<List<Morador>>
}