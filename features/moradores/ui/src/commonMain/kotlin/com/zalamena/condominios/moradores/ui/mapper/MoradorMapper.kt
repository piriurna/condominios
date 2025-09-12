package com.zalamena.condominios.moradores.ui.mapper

import com.zalamena.condominios.apartamentos.domain.models.Apartamento
import com.zalamena.condominios.apartamentos.ui.mapper.toUi
import com.zalamena.condominios.pessoa.ui.mapper.toUi
import com.zalamena.condominios.moradores.ui.models.MoradorUiData
import com.zalamena.condominios.moradores.ui.models.SelectApartamentoUiData
import com.zalamena.condominios.moradores.ui.models.SelectPessoaUiData
import com.zalamena.condominios.pessoa.domain.models.Pessoa
import com.zalamena.moradores.domain.models.Morador

fun Morador.toUi(): MoradorUiData {
    return MoradorUiData(
        morador = pessoa.toUi(),
        apartamento = apartamento.toUi(),
    )
}

fun Pessoa.toSelectUi(): SelectPessoaUiData {
    return SelectPessoaUiData(
        id = id,
        nome = nome,
        cpf = cpf
    )
}


fun Apartamento.toSelectUi(): SelectApartamentoUiData {
    return SelectApartamentoUiData(
        id = id,
        numero = numero,
    )
}