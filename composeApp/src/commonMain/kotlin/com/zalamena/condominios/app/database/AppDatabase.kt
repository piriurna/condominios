package com.zalamena.condominios.app.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.InvalidationTracker
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.zalamena.condominios.moradores.data.dao.MoradoresDao
import com.zalamena.condominios.moradores.data.entities.LoggedMoradorEntity
import com.zalamena.condominios.moradores.data.entities.MoradorEntity

@Database(
    entities = [
        MoradorEntity::class,
        LoggedMoradorEntity::class,
   ],
    version = 1
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase(), DB {
    override fun createInvalidationTracker(): InvalidationTracker {
        TODO("Not yet implemented")
    }

    override fun clearAllTables() {
    }

    abstract fun getMoradoresDao(): MoradoresDao
}
@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

interface DB {
    fun clearAllTables(): Unit {}
}