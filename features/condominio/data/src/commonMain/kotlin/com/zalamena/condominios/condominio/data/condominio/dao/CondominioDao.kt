package com.zalamena.condominios.condominio.data.condominio.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Transaction
import com.zalamena.condominios.condominio.data.condominio.entity.CondominioEntity
import com.zalamena.condominios.condominio.data.condominio.entity.CondominioWithAllData
import com.zalamena.condominios.condominio.data.condominio.entity.EnderecoEntity

@Dao
interface CondominioDao {

    @Transaction
    @Query("SELECT * FROM Condominio")
    suspend fun getCondominios(): List<CondominioWithAllData>

    @Insert
    suspend fun insertCondominio(condominio: CondominioEntity)

    @Transaction
    @Query(
        """
            SELECT *
            FROM Condominio
            WHERE id = :condominioId
        """
    )
    suspend fun getCondominio(condominioId: String): CondominioWithAllData?

    @Query(
        """
            SELECT *
            FROM EnderecoEntity
            WHERE id = :enderecoId
        """
    )
    suspend fun getEndereco(enderecoId: String): EnderecoEntity?

    @Insert(onConflict = REPLACE)
    suspend fun insertEndereco(endereco: EnderecoEntity)

}