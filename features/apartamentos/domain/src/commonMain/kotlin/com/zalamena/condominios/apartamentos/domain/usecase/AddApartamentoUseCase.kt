package com.zalamena.condominios.apartamentos.domain.usecase

import com.zalamena.condominios.apartamentos.domain.mapper.toApartamento
import com.zalamena.condominios.apartamentos.domain.models.AddApartamentoForm
import com.zalamena.condominios.apartamentos.domain.repository.ApartamentosRepository

class AddApartamentoUseCase(
    private val apartamentosRepository: ApartamentosRepository
) {
    suspend operator fun invoke(apartamentoForm: AddApartamentoForm): Result<Unit> {
        val apartamentoIdResult =
            apartamentosRepository.createApartamentoId(apartamentoForm.numero)

        return if(apartamentoIdResult.isSuccess) {
            val id = apartamentoIdResult.getOrThrow()

            apartamentosRepository.addApartamento(apartamentoForm.toApartamento(id))
        } else {
            Result.failure(apartamentoIdResult.exceptionOrNull()!!)
        }
    }
}