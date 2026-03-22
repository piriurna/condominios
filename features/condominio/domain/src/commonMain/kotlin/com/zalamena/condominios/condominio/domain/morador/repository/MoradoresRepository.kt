package com.zalamena.condominios.condominio.domain.morador.repository

import com.zalamena.condominios.condominio.domain.morador.model.Morador
import com.zalamena.condominios.condominio.domain.morador.model.MoradorTipo

interface MoradoresRepository {
    suspend fun addMorador(pessoaId: String, apartamentoId: String, tipo: MoradorTipo): Result<Unit>

    suspend fun getMorador(id: String, apartamentoId: String): Result<Morador>

    suspend fun getMoradoresForApartamento(apartamentoId: String): Result<List<Morador>>

    suspend fun getMoradoresForCondominio(condominioId: String): Result<List<Morador>>

    suspend fun getMoradoresForPessoa(pessoaId: String): Result<List<Morador>>

    suspend fun removeMorador(pessoaId: String, apartamentoId: String): Result<Unit>

    suspend fun updateMoradorTipo(pessoaId: String, apartamentoId: String, newTipo: MoradorTipo): Result<Unit>
}
