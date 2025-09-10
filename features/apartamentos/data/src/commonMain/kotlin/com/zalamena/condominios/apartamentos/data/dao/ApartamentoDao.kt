package com.zalamena.condominios.apartamentos.data.dao

import androidx.room.Insert
import androidx.room.Query
import com.zalamena.condominios.apartamentos.data.entity.ApartamentoEntity

interface ApartamentoDao {
    @Query("SELECT * FROM Apartamento WHERE id = :apartamentoId")
    suspend fun getApartamento(apartamentoId: String): ApartamentoEntity?


    @Insert
    suspend fun addApartamento(apartamento: ApartamentoEntity)
}