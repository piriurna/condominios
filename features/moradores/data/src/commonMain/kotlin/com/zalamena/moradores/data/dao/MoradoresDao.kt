package com.zalamena.moradores.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.zalamena.moradores.data.entities.MoradorEntity

@Dao
interface MoradoresDao {

    @Query("SELECT * FROM Morador")
    fun getAllMoradores(): List<MoradorEntity>


    @Query("SELECT * FROM Morador WHERE cpf = :cpf LIMIT 1")
    fun getMorador(cpf: String): MoradorEntity?

    @Insert
    fun addMorador(morador: MoradorEntity)

    @Query("SELECT * FROM Morador WHERE apartamentoId =:apartamentoId")
    fun getAllMoradoresForApartamento(apartamentoId: String): List<MoradorEntity>
}