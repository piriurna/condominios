package com.zalamena.condominios.condominio.domain.apartamento.usecase

import com.zalamena.condominios.condominio.domain.apartamento.models.Apartamento

interface GetApartamentoUseCase {
    suspend operator fun invoke(apartamentoId: String): Result<Apartamento>
}