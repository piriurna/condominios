package com.zalamena.condominios.condominio.domain.apartamento.usecase

import com.zalamena.condominios.condominio.domain.apartamento.mapper.toApartamento
import com.zalamena.condominios.condominio.domain.apartamento.model.AddApartamentoForm
import com.zalamena.condominios.condominio.domain.apartamento.repository.ApartamentosRepository

class AddApartamentoUseCase(
    private val apartamentosRepository: ApartamentosRepository
) {
    suspend operator fun invoke(apartamentoForm: AddApartamentoForm): Result<String> {
        val apartamentoIdResult =
            apartamentosRepository.createApartamentoId(apartamentoForm.numero)

        return if (apartamentoIdResult.isSuccess) {
            val id = apartamentoIdResult.getOrThrow()

            val addResult = apartamentosRepository.addApartamento(
                condominioId = apartamentoForm.condominioId,
                apartamento = apartamentoForm.toApartamento(id)
            )

            if (addResult.isSuccess) {
                Result.success(id)
            } else {
                Result.failure(addResult.exceptionOrNull()!!)
            }
        } else {
            Result.failure(apartamentoIdResult.exceptionOrNull()!!)
        }
    }
}
