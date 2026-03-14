package com.zalamena.condominios.condominio.domain.addapartamento.usecases

import com.zalamena.condominios.condominio.domain.addapartamento.mapper.toApartamento
import com.zalamena.condominios.condominio.domain.addapartamento.models.AddApartamentoForm
import com.zalamena.condominios.condominio.domain.apartamento.repository.ApartamentosRepository

class AddApartamentoUseCase(
    private val apartamentosRepository: ApartamentosRepository
) {
    suspend operator fun invoke(apartamentoForm: AddApartamentoForm): Result<String> {
        val apartamentoIdResult =
            apartamentosRepository.createApartamentoId(apartamentoForm.numero) // Maybe create a service for this

        return if(apartamentoIdResult.isSuccess) {
            val id = apartamentoIdResult.getOrThrow()

            val addResult = apartamentosRepository.addApartamento(
                condominioId = apartamentoForm.condominioId,
                apartamentoForm.toApartamento(id)
            )

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