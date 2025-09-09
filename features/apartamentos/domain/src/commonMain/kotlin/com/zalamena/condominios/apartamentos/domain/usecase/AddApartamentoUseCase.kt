package com.zalamena.condominios.apartamentos.domain.usecase

import com.zalamena.condominios.apartamentos.domain.models.Apartamento
import com.zalamena.condominios.apartamentos.domain.models.ApartamentoException
import com.zalamena.condominios.apartamentos.domain.repository.ApartamentosRepository

class AddApartamentoUseCase(
    private val apartamentosRepository: ApartamentosRepository
) {
    suspend operator fun invoke(apartamento: Apartamento): Result<Unit> {
        val registeredApartamento = apartamentosRepository.getApartamento(apartamento.id)

        if(registeredApartamento.exceptionOrNull() == ApartamentoException.NoApartmentFoundException) {
            apartamentosRepository.addApartamento(apartamento)
            return Result.success(Unit)
        }

        return Result.failure(ApartamentoException.DuplicatedApartmentException)
    }
}