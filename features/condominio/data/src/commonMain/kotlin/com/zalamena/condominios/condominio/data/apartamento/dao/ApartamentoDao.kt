package com.zalamena.condominios.condominio.data.apartamento.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.zalamena.condominios.condominio.data.apartamento.entity.ApartamentoEntity
import com.zalamena.condominios.condominio.data.apartamento.entity.ApartamentoWithAllData

@Dao
interface ApartamentoDao {
    @Transaction
    @Query("SELECT * FROM Apartamento WHERE id = :apartamentoId")
    suspend fun getApartamento(apartamentoId: String): ApartamentoWithAllData?


    @Transaction
    @Query("SELECT * FROM Apartamento")
    suspend fun getApartamentos(): List<ApartamentoWithAllData>


    @Insert
    suspend fun addApartamento(apartamento: ApartamentoEntity)

    @Query("SELECT condominioId FROM Apartamento WHERE id = :apartamentoId")
    suspend fun getCondominioIdForApartamento(apartamentoId: String): String?
}