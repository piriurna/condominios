package com.zalamena.condominios.pessoa.domain.addpessoa.mapper

import com.zalamena.condominios.pessoa.domain.addpessoa.models.AddPessoaForm
import com.zalamena.condominios.pessoa.domain.models.Pessoa

fun AddPessoaForm.toPessoa(id: String) =
    Pessoa(
        id = id,
        nome = nome,
        cpf = cpf,
        telefone = telefone,
        email = email
    )