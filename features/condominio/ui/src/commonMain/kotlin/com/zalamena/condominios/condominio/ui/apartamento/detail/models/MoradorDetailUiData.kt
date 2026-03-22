package com.zalamena.condominios.condominio.ui.apartamento.detail.models

import com.zalamena.condominios.condominio.domain.morador.model.MoradorTipo

data class MoradorDetailUiData(
    val pessoaId: String,
    val apartamentoId: String = "",
    val nome: String,
    val maskedCpf: String,
    val tipoLabel: String
)

fun MoradorTipo.toLabel(): String = when (this) {
    MoradorTipo.PROPRIETARIO -> "Proprietário"
    MoradorTipo.RESIDENTE -> "Morador"
    MoradorTipo.RESIDENTE_TEMPORARIO -> "Temporário"
}

/**
 * Masks a CPF string, showing only the last 2 digits.
 * Input can be raw (11 digits) or formatted (###.###.###-##).
 * Output: ***.***.***-XX
 */
fun maskCpf(cpf: String): String {
    val digits = cpf.filter { it.isDigit() }
    if (digits.length < 2) return "***.***.***-**"
    val last2 = digits.takeLast(2)
    return "***.***.***-$last2"
}
