package com.zalamena.condominios.individuo.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.zalamena.condominios.individuo.data.entities.IndividuoEntity

@Dao
interface IndividuoDao {

    @Insert
    suspend fun addIndividuo(individuo: IndividuoEntity)


    @Query("SELECT * FROM individuo WHERE id = :id")
    suspend fun getIndividuo(id: String): IndividuoEntity?


    @Query("SELECT * FROM individuo")
    suspend fun getAllIndividos(): List<IndividuoEntity>


}