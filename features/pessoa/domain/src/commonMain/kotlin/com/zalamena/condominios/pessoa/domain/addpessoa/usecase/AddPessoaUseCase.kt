package com.zalamena.condominios.pessoa.domain.addpessoa.usecase

import com.zalamena.condominios.pessoa.domain.addpessoa.models.AddPessoaForm

interface AddPessoaUseCase {


    suspend operator fun invoke(pessoaForm: AddPessoaForm): Result<String>
}