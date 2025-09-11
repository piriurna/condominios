package com.zalamena.condominios.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.zalamena.condominios.apartamentos.data.dao.ApartamentoDao
import com.zalamena.condominios.apartamentos.data.entity.ApartamentoEntity
import com.zalamena.condominios.pessoa.data.dao.PessoaDao
import com.zalamena.condominios.pessoa.data.entities.PessoaEntity
import com.zalamena.moradores.data.dao.MoradoresDao
import com.zalamena.moradores.data.entities.MoradorEntity

@Database(entities = [
    MoradorEntity::class,
    ApartamentoEntity::class,
    PessoaEntity::class
], version = 1)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getMoradoresDao(): MoradoresDao
    abstract fun getApartamentosDao(): ApartamentoDao
    abstract fun getPessoaDao(): PessoaDao
}

// The Room compiler generates the `actual` implementations.
@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}