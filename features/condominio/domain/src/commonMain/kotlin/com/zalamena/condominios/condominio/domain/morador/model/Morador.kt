package com.zalamena.condominios.condominio.domain.morador.model

import com.zalamena.condominios.condominio.domain.apartamento.models.Apartamento
import com.zalamena.condominios.pessoa.domain.models.Pessoa
import com.zalamena.condominios.pessoa.domain.models.PessoaProperties

data class Morador(
    val pessoa: Pessoa,
    val apartamento: Apartamento
) : PessoaProperties by pessoa {

    companion object {
        val dummy = Morador(
            pessoa = Pessoa.dummy,
            apartamento = Apartamento.dummy
        )
    }
}
