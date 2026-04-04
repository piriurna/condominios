package com.zalamena.condominios.condominio.data.amenity.repository

import com.zalamena.condominios.condominio.data.amenity.dao.AmenityDao
import com.zalamena.condominios.condominio.data.amenity.mapper.toDomain
import com.zalamena.condominios.condominio.data.amenity.mapper.toEntity
import com.zalamena.condominios.condominio.domain.amenity.model.Amenity
import com.zalamena.condominios.condominio.domain.amenity.repository.AmenityRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class AmenityRepositoryImpl(
    private val amenityDao: AmenityDao
) : AmenityRepository {

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun addAmenity(amenity: Amenity): Result<Unit> {
        return runCatching {
            val id = Uuid.random().toString()
            amenityDao.insertAmenity(amenity.copy(id = id).toEntity())
        }
    }

    override suspend fun getAmenitiesByCondominio(condominioId: String): Result<List<Amenity>> {
        return runCatching {
            amenityDao.getAmenitiesByCondominio(condominioId).map { it.toDomain() }
        }
    }
}
