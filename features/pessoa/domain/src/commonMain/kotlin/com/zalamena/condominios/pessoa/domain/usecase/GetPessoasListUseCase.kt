package com.zalamena.condominios.pessoa.domain.usecase

import com.zalamena.condominios.pessoa.domain.models.Pessoa
import com.zalamena.condominios.pessoa.domain.repository.PessoaRepository

class GetPessoasListUseCase(
    private val pessoaRepository: PessoaRepository
) {


    suspend operator fun invoke(): Result<List<Pessoa>> {
        return pessoaRepository.getAllIndividos()
    }
}