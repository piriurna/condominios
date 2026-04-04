package com.zalamena.condominios.condominio.domain.amenity.repository

import com.zalamena.condominios.condominio.domain.amenity.model.Amenity

interface AmenityRepository {
    suspend fun addAmenity(amenity: Amenity): Result<Unit>
    suspend fun getAmenitiesByCondominio(condominioId: String): Result<List<Amenity>>
}
