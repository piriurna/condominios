package com.zalamena.condominios.addpessoa.ui.mapper

import com.zalamena.condominios.addpessoa.domain.models.AddPessoaForm
import com.zalamena.condominios.addpessoa.ui.models.AddPessoaFormUiData

fun AddPessoaFormUiData.toDomain(): AddPessoaForm {
    return AddPessoaForm(
        nome = nome?:"",
        cpf = cpf?:"",
        email = email?:"",
        telefone = telefone?:""
    )
}