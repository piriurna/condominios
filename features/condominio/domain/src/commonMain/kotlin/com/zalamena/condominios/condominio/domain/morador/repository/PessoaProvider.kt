package com.zalamena.condominios.condominio.domain.morador.repository

import com.zalamena.condominios.pessoa.domain.models.Pessoa

fun interface PessoaProvider {
    suspend fun getAllPessoas(): Result<List<Pessoa>>
}
