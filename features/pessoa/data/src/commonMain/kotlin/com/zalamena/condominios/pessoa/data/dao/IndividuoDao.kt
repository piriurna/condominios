package com.zalamena.condominios.pessoa.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.zalamena.condominios.pessoa.data.entities.PessoaEntity

@Dao
interface PessoaDao {

    @Insert
    suspend fun addPessoa(pessoa: PessoaEntity)


    @Query("SELECT * FROM pessoa WHERE id = :id")
    suspend fun getPessoa(id: String): PessoaEntity?


    @Query("SELECT * FROM pessoa")
    suspend fun getAllIndividos(): List<PessoaEntity>


}