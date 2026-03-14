package com.zalamena.condominios.condominio.domain.condominio.usecase

import com.zalamena.condominios.condominio.domain.condominio.repository.CondominioRepository
import com.zalamena.condominios.condominio.domain.moradores.models.Morador

class GetMoradoresUseCase(
    private val condominioRepository: CondominioRepository
) {
    suspend operator fun invoke(condominioId: String): Result<List<Morador>> {
        return runCatching {
            condominioRepository
                .getCondominio(condominioId)
                .getOrThrow()
                .apartamentos
                .flatMap { apartamento ->
                    apartamento.moradores.map { morador ->
                        Morador(
                            pessoa = morador,
                            apartamento = apartamento
                        )
                    }
                }
        }
    }
}