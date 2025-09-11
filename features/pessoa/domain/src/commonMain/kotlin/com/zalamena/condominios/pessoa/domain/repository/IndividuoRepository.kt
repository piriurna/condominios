package com.zalamena.condominios.pessoa.domain.repository

import com.zalamena.condominios.pessoa.domain.models.Pessoa

interface PessoaRepository {
    suspend fun addPessoa(pessoa: Pessoa): Result<Pessoa>

    suspend fun getPessoa(cpf: String): Result<Pessoa>

    suspend fun getAllIndividos(): Result<List<Pessoa>>
}