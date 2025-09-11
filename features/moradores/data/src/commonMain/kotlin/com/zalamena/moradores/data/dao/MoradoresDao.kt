package com.zalamena.moradores.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import androidx.room.Transaction
import com.zalamena.moradores.data.entities.MoradorEntity
import com.zalamena.moradores.data.entities.MoradorWithPessoaAndApartamentoEntity

@Dao
interface MoradoresDao {

    @Transaction
    @Query("SELECT * FROM Morador")
    @RewriteQueriesToDropUnusedColumns
    suspend fun getAllMoradores(): List<MoradorWithPessoaAndApartamentoEntity>


    @Transaction
    @Query("""
        SELECT Morador.* FROM Morador
        LEFT JOIN Pessoa ON Morador.pessoaId = Pessoa.id
        WHERE cpf = :cpf AND  apartamentoId =:apartamentoId
        LIMIT 1
        """)
    @RewriteQueriesToDropUnusedColumns
    suspend fun getMorador(cpf: String, apartamentoId: String): MoradorWithPessoaAndApartamentoEntity?

    @Insert
    suspend fun addMorador(morador: MoradorEntity)

    @Transaction
    @Query("SELECT * FROM Morador WHERE apartamentoId =:apartamentoId")
    @RewriteQueriesToDropUnusedColumns
    suspend fun getAllMoradoresForApartamento(apartamentoId: String): List<MoradorWithPessoaAndApartamentoEntity>
}