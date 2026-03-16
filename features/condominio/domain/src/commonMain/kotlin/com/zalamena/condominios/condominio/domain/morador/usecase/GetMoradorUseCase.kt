package com.zalamena.condominios.condominio.domain.morador.usecase

import com.zalamena.condominios.condominio.domain.morador.model.Morador
import com.zalamena.condominios.condominio.domain.morador.repository.MoradoresRepository

class GetMoradorUseCase(
    private val moradoresRepository: MoradoresRepository
) {
    suspend operator fun invoke(cpf: String, apartamentoId: String): Result<Morador> {
        return moradoresRepository.getMorador(cpf, apartamentoId).getOrNull()?.let {
            Result.success(it)
        } ?: Result.failure(Exception("Morador não encontrado"))
    }
}
