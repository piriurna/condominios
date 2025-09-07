package com.zalamena.moradores.domain.repository

import com.zalamena.moradores.domain.models.Morador

interface MoradoresRepository {

    fun getCurrentLoggedMorador(): Morador?


    fun loginMorador(morador: Morador)


    fun addMorador(morador: Morador)

    fun getMorador(morador: Morador): Morador?

    fun getAllMoradores(): List<Morador>
}