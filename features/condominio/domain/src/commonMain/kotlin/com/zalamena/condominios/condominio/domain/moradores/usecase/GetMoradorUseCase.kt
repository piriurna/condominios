package com.zalamena.condominios.condominio.domain.moradores.usecase

import com.zalamena.condominios.condominio.domain.moradores.models.Morador
import com.zalamena.condominios.condominio.domain.moradores.repository.MoradoresRepository

class GetMoradorUseCase(
    private val moradoresRepository: MoradoresRepository
) {
    suspend operator fun invoke(cpf: String, apartamentoId: String): Result<Morador> {
        return moradoresRepository.getMorador(cpf, apartamentoId).getOrNull()?.let {
            Result.success(it)
        }?: Result.failure(Exception("Morador não encontrado"))
    }
}