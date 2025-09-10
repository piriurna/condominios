package com.zalamena.moradores.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.zalamena.moradores.data.entities.MoradorEntity
import com.zalamena.moradores.data.entities.MoradorWithIndividuoAndApartamentoEntity

@Dao
interface MoradoresDao {

    @Query("SELECT * FROM Morador")
    fun getAllMoradores(): List<MoradorWithIndividuoAndApartamentoEntity>


    @Query("""
        SELECT * FROM Morador
        LEFT JOIN Individuo ON Morador.individuoId = Individuo.id
        WHERE cpf = :cpf AND  apartamentoId =:apartamentoId
        LIMIT 1
        """)
    fun getMorador(cpf: String, apartamentoId: String): MoradorWithIndividuoAndApartamentoEntity?

    @Insert
    fun addMorador(morador: MoradorEntity)

    @Query("SELECT * FROM Morador WHERE apartamentoId =:apartamentoId")
    fun getAllMoradoresForApartamento(apartamentoId: String): List<MoradorWithIndividuoAndApartamentoEntity>
}