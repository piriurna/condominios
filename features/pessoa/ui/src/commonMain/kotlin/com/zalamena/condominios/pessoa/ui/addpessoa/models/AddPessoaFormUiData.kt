package com.zalamena.condominios.pessoa.ui.addpessoa.models

import com.zalamena.condominios.pessoa.domain.addpessoa.models.AddPessoaFormError

data class AddPessoaFormUiData(
    val nome: String = "",
    val cpf: String = "",
    val email: String = "",
    val telefone: String = "",
    val formErrors: List<AddPessoaFormError> = emptyList()
) {
}