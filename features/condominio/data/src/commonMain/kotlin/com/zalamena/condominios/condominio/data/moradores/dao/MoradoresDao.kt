package com.zalamena.condominios.condominio.data.moradores.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.ABORT
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import androidx.room.Transaction
import com.zalamena.condominios.condominio.data.moradores.entities.MoradorEntity
import com.zalamena.condominios.condominio.data.moradores.entities.MoradorWithPessoaAndApartamentoEntity

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
        WHERE Pessoa.id = :id AND apartamentoId =:apartamentoId
        LIMIT 1
        """)
    @RewriteQueriesToDropUnusedColumns
    suspend fun getMorador(id: String, apartamentoId: String): MoradorWithPessoaAndApartamentoEntity?

    @Insert(onConflict = ABORT)
    suspend fun addMorador(morador: MoradorEntity)

    @Transaction
    @Query("SELECT * FROM Morador WHERE apartamentoId =:apartamentoId")
    @RewriteQueriesToDropUnusedColumns
    suspend fun getAllMoradoresForApartamento(apartamentoId: String): List<MoradorWithPessoaAndApartamentoEntity>

    @Transaction
    @Query("""
        SELECT Morador.* FROM Morador
        LEFT JOIN Apartamento ON Morador.apartamentoId = Apartamento.id
        WHERE Apartamento.condominioId = :condominioId
    """)
    @RewriteQueriesToDropUnusedColumns
    suspend fun getAllMoradoresForCondominio(condominioId: String): List<MoradorWithPessoaAndApartamentoEntity>

    @Transaction
    @Query("SELECT * FROM Morador WHERE pessoaId = :pessoaId")
    @RewriteQueriesToDropUnusedColumns
    suspend fun getAllMoradoresForPessoa(pessoaId: String): List<MoradorWithPessoaAndApartamentoEntity>

    @Query("DELETE FROM Morador WHERE pessoaId = :pessoaId AND apartamentoId = :apartamentoId")
    suspend fun deleteMorador(pessoaId: String, apartamentoId: String)

    @Query("UPDATE Morador SET tipo = :tipo WHERE pessoaId = :pessoaId AND apartamentoId = :apartamentoId")
    suspend fun updateMoradorTipo(pessoaId: String, apartamentoId: String, tipo: String)
}