package com.zalamena.condominios.addapartamento.domain.usecases

import com.zalamena.condominios.addapartamento.domain.mapper.toApartamento
import com.zalamena.condominios.addapartamento.domain.models.AddApartamentoForm
import com.zalamena.condominios.apartamentos.domain.repository.ApartamentosRepository

class AddApartamentoUseCase(
    private val apartamentosRepository: ApartamentosRepository
) {
    suspend operator fun invoke(apartamentoForm: AddApartamentoForm): Result<String> {
        val apartamentoIdResult =
            apartamentosRepository.createApartamentoId(apartamentoForm.numero) // Maybe create a service for this

        return if(apartamentoIdResult.isSuccess) {
            val id = apartamentoIdResult.getOrThrow()

            val addResult = apartamentosRepository.addApartamento(apartamentoForm.toApartamento(id))

            if(addResult.isSuccess) {
                Result.success(id)
            } else {
                Result.failure(addResult.exceptionOrNull()!!)
            }
        } else {
            Result.failure(apartamentoIdResult.exceptionOrNull()!!)
        }
    }
}