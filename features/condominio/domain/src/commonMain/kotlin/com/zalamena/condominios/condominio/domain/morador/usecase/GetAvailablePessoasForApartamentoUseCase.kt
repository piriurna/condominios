package com.zalamena.condominios.condominio.domain.morador.usecase

import com.zalamena.condominios.condominio.domain.morador.repository.PessoaProvider
import com.zalamena.condominios.pessoa.domain.models.Pessoa

class GetAvailablePessoasForApartamentoUseCase(
    private val pessoaProvider: PessoaProvider,
    private val getMoradoresForApartamentoUseCase: GetMoradoresForApartamentoUseCase
) {
    suspend operator fun invoke(apartamentoId: String?): Result<List<Pessoa>> {
        val pessoasResult = pessoaProvider.getAllPessoas()
        val pessoas = pessoasResult.getOrElse { return pessoasResult }

        if (apartamentoId == null) {
            return Result.success(pessoas)
        }

        val existingPessoaIds = getMoradoresForApartamentoUseCase(apartamentoId)
            .getOrElse { emptyList() }
            .map { it.pessoa.id }
            .toSet()

        return Result.success(pessoas.filter { it.id !in existingPessoaIds })
    }
}
