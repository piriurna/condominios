package com.zalamena.condominios.addapartamento.domain.usecases

import com.zalamena.condominios.addapartamento.domain.mapper.toApartamento
import com.zalamena.condominios.addapartamento.domain.models.AddApartamentoForm
import com.zalamena.condominios.apartamentos.domain.repository.ApartamentosRepository

class AddApartamentoUseCase(
    private val apartamentosRepository: ApartamentosRepository
) {
    suspend operator fun invoke(apartamentoForm: AddApartamentoForm): Result<Unit> {
        val apartamentoIdResult =
            apartamentosRepository.createApartamentoId(apartamentoForm.numero) // Maybe create a service for this

        return if(apartamentoIdResult.isSuccess) {
            val id = apartamentoIdResult.getOrThrow()

            apartamentosRepository.addApartamento(apartamentoForm.toApartamento(id))
        } else {
            Result.failure(apartamentoIdResult.exceptionOrNull()!!)
        }
    }
}