package com.zalamena.condominios.condominio.domain.apartamento.usecase

import com.zalamena.condominios.condominio.domain.apartamento.mapper.toApartamento
import com.zalamena.condominios.condominio.domain.apartamento.model.AddApartamentoForm
import com.zalamena.condominios.condominio.domain.apartamento.repository.ApartamentosRepository

class AddApartamentoUseCase(
    private val apartamentosRepository: ApartamentosRepository
) {
    suspend operator fun invoke(apartamentoForm: AddApartamentoForm): Result<String> {
        return apartamentosRepository.addApartamento(
            condominioId = apartamentoForm.condominioId,
            apartamento = apartamentoForm.toApartamento(id = "")
        )
    }
}
