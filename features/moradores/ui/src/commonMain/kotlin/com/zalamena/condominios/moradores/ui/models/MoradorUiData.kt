package com.zalamena.condominios.moradores.ui.models

import com.zalamena.condominios.apartamentos.ui.models.ApartamentoUiData
import com.zalamena.condominios.pessoa.ui.models.PessoaUiData
import com.zalamena.condominios.pessoa.ui.models.PessoaUiProperties

data class MoradorUiData(
    val morador: PessoaUiData,
    val apartamento: ApartamentoUiData
): PessoaUiProperties by morador {

    companion object {
        val dummy = MoradorUiData(
            morador = PessoaUiData.dummy,
            apartamento = ApartamentoUiData.dummy
        )
    }
}