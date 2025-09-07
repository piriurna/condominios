package com.zalamena.condominios.moradores.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.zalamena.condominios.moradores.data.entities.LoggedMoradorEntity
import com.zalamena.condominios.moradores.data.entities.MoradorEntity

@Dao
interface MoradoresDao {

    @Query("""
        SELECT m.* FROM LoggedMorador LEFT JOIN Morador m ON LoggedMorador.moradorCpf = m.cpf
    """)
    fun getLoggedInMorador(): MoradorEntity?


    @Insert
    fun setLoggedInMorador(
        moradorCpf: String,
        timestamp: Long
    )


    @Query("SELECT * FROM Morador")
    fun getAllMoradores(): List<MoradorEntity>



    @Insert
    fun addMorador(morador: MoradorEntity)
}