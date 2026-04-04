package com.zalamena.condominios.condominio.data.amenity.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.zalamena.condominios.condominio.data.amenity.entity.AmenityEntity

@Dao
interface AmenityDao {
    @Insert
    suspend fun insertAmenity(amenity: AmenityEntity)

    @Query("SELECT * FROM Amenity WHERE condominioId = :condominioId")
    suspend fun getAmenitiesByCondominio(condominioId: String): List<AmenityEntity>
}
