package com.zalamena.condominios.addpessoa.domain.usecase

import com.zalamena.condominios.addpessoa.domain.models.AddPessoaForm

interface AddPessoaUseCase {


    suspend operator fun invoke(pessoaForm: AddPessoaForm): Result<String>
}