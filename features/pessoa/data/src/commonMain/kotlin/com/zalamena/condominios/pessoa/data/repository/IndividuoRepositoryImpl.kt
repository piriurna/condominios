package com.zalamena.condominios.pessoa.data.repository

import com.zalamena.condominios.pessoa.data.dao.PessoaDao
import com.zalamena.condominios.pessoa.data.mapper.toDomain
import com.zalamena.condominios.pessoa.data.mapper.toEntity
import com.zalamena.condominios.pessoa.domain.models.Pessoa
import com.zalamena.condominios.pessoa.domain.models.PessoaException
import com.zalamena.condominios.pessoa.domain.repository.PessoaRepository

class PessoaRepositoryImpl(
    private val pessoaDao: PessoaDao
): PessoaRepository {
    override suspend fun addPessoa(pessoa: Pessoa): Result<Pessoa> {
        return runCatching {
            pessoaDao.addPessoa(pessoa.toEntity())

            return@runCatching pessoa
        }
    }

    override suspend fun getPessoa(cpf: String): Result<Pessoa> {
        return runCatching {
            return@runCatching pessoaDao.getPessoa(cpf)?.toDomain()
                ?: throw PessoaException.PessoaNotFoundException
        }
    }

    override suspend fun getAllIndividos(): Result<List<Pessoa>> {
        return runCatching {
            return@runCatching pessoaDao.getAllIndividos().map { it.toDomain() }
        }
    }
}