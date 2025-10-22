package com.zalamena.condominios.pessoa.ui.addpessoa.mapper

import com.zalamena.condominios.pessoa.domain.addpessoa.models.AddPessoaForm
import com.zalamena.condominios.pessoa.ui.addpessoa.models.AddPessoaFormUiData

fun AddPessoaFormUiData.toDomain(): AddPessoaForm {
    return AddPessoaForm(
        nome = nome?:"",
        cpf = cpf?:"",
        email = email?:"",
        telefone = telefone?:""
    )
}