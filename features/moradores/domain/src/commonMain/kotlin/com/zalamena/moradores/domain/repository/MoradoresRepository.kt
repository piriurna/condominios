package com.zalamena.moradores.domain.repository

import com.zalamena.moradores.domain.models.Morador

interface MoradoresRepository {

    fun getCurrentLoggedMorador(): Morador?

    fun addMorador(morador: Morador)

    fun getMorador(cpf: String): Morador?

    fun getAllMoradores(): List<Morador>
}