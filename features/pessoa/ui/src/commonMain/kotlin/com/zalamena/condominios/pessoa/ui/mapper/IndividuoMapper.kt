package com.zalamena.condominios.pessoa.ui.mapper

import com.zalamena.condominios.pessoa.domain.models.Pessoa
import com.zalamena.condominios.pessoa.ui.models.PessoaUiData

fun Pessoa.toUi(): PessoaUiData {
    return PessoaUiData(
        id = id,
        nome = nome,
        cpf = cpf,
    )
}