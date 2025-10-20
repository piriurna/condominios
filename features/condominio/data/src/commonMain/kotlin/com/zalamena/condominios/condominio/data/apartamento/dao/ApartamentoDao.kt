package com.zalamena.condominios.condominio.data.apartamento.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.zalamena.condominios.condominio.data.apartamento.entity.ApartamentoEntity

@Dao
interface ApartamentoDao {
    @Query("SELECT * FROM Apartamento WHERE id = :apartamentoId")
    suspend fun getApartamento(apartamentoId: String): ApartamentoEntity?


    @Query("SELECT * FROM Apartamento")
    suspend fun getApartamentos(): List<ApartamentoEntity>


    @Insert
    suspend fun addApartamento(apartamento: ApartamentoEntity)
}