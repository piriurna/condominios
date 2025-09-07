package com.zalamena.condominios.moradores.database

import com.zalamena.condominios.moradores.data.dao.MoradoresDao
import com.zalamena.condominios.moradores.data.entities.LoggedMoradorEntity
import com.zalamena.condominios.moradores.data.entities.MoradorEntity

class MoradoresDaoTest: MoradoresDao {

    var loggedUser: LoggedMoradorEntity? = null
    var existingUsers: MutableList<MoradorEntity> = mutableListOf()

    override fun getLoggedInMorador(): MoradorEntity? {
        return existingUsers.first { it.cpf == loggedUser?.moradorCpf }
    }

    override fun setLoggedInMorador(moradorCpf: String, timestamp: Long) {
        loggedUser = LoggedMoradorEntity(moradorCpf, timestamp)
    }

    override fun getAllMoradores(): List<MoradorEntity> {
        return existingUsers
    }

    override fun addMorador(morador: MoradorEntity) {
        existingUsers.add(morador)
    }
}