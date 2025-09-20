package com.zalamena.condominios.addpessoa.ui.models

import com.zalamena.condominios.addpessoa.domain.models.AddPessoaFormError

data class AddPessoaFormUiData(
    val nome: String = "",
    val cpf: String = "",
    val email: String = "",
    val telefone: String = "",
    val formErrors: List<AddPessoaFormError> = emptyList()
) {
}