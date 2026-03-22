package com.zalamena.condominios.condominio.domain.morador.usecase

import com.zalamena.condominios.condominio.domain.morador.model.Morador
import com.zalamena.condominios.condominio.domain.morador.repository.MoradoresRepository

class GetMoradorDetailUseCase(
    private val moradoresRepository: MoradoresRepository
) {
    suspend operator fun invoke(pessoaId: String): Result<List<Morador>> =
        moradoresRepository.getMoradoresForPessoa(pessoaId)
}
