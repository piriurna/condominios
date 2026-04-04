package com.zalamena.condominios.condominio.domain.amenity.usecase

import com.zalamena.condominios.condominio.domain.amenity.model.Amenity
import com.zalamena.condominios.condominio.domain.amenity.repository.AmenityRepository

class GetAmenitiesByCondominioUseCase(
    private val amenityRepository: AmenityRepository
) {
    suspend operator fun invoke(condominioId: String): Result<List<Amenity>> {
        return amenityRepository.getAmenitiesByCondominio(condominioId)
    }
}
