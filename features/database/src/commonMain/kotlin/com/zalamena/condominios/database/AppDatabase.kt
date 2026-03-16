package com.zalamena.condominios.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.zalamena.condominios.condominio.data.apartamento.dao.ApartamentoDao
import com.zalamena.condominios.condominio.data.apartamento.entity.ApartamentoEntity
import com.zalamena.condominios.condominio.data.condominio.dao.CondominioDao
import com.zalamena.condominios.condominio.data.condominio.entity.CondominioEntity
import com.zalamena.condominios.condominio.data.condominio.entity.EnderecoEntity
import com.zalamena.condominios.condominio.data.moradores.dao.MoradoresDao
import com.zalamena.condominios.condominio.data.moradores.entities.MoradorEntity
import com.zalamena.condominios.pessoa.data.dao.PessoaDao
import com.zalamena.condominios.pessoa.data.entities.PessoaEntity

@Database(entities = [
    MoradorEntity::class,
    ApartamentoEntity::class,
    CondominioEntity::class,
    EnderecoEntity::class,
    PessoaEntity::class
], version = 2)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getMoradoresDao(): MoradoresDao
    abstract fun getApartamentosDao(): ApartamentoDao
    abstract fun getCondominioDao(): CondominioDao
    abstract fun getPessoaDao(): PessoaDao
}

// The Room compiler generates the `actual` implementations.
@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}