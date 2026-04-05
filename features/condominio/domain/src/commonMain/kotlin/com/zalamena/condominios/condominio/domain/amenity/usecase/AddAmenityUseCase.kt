package com.zalamena.condominios.condominio.domain.amenity.usecase

import com.zalamena.condominios.condominio.domain.amenity.model.Amenity
import com.zalamena.condominios.condominio.domain.amenity.repository.AmenityRepository

class AddAmenityUseCase(
    private val amenityRepository: AmenityRepository
) {
    suspend operator fun invoke(amenity: Amenity): Result<Unit> {
        if (amenity.nome.isBlank()) {
            return Result.failure(IllegalArgumentException("Nome e obrigatorio"))
        }
        if (amenity.condominioId.isBlank()) {
            return Result.failure(IllegalArgumentException("condominioId e obrigatorio"))
        }
        return amenityRepository.addAmenity(amenity)
    }
}
