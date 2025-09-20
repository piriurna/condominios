package com.zalamena.condominios.apartamentos.domain.usecase

import com.zalamena.condominios.apartamentos.domain.models.Apartamento

interface GetApartamentoUseCase {
    suspend operator fun invoke(apartamentoId: String): Result<Apartamento>
}