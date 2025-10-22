package com.zalamena.condominios.condominio.ui.moradores.mapper

import com.zalamena.condominios.condominio.domain.apartamento.models.Apartamento
import com.zalamena.condominios.condominio.domain.moradores.models.Morador
import com.zalamena.condominios.condominio.ui.apartamentos.mapper.toUi
import com.zalamena.condominios.condominio.ui.moradores.models.MoradorUiData
import com.zalamena.condominios.condominio.ui.moradores.models.SelectApartamentoUiData
import com.zalamena.condominios.condominio.ui.moradores.models.SelectPessoaUiData
import com.zalamena.condominios.pessoa.domain.models.Pessoa
import com.zalamena.condominios.pessoa.ui.mapper.toUi

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