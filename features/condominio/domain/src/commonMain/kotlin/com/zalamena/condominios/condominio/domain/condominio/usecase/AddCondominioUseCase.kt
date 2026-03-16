package com.zalamena.condominios.condominio.domain.condominio.usecase

import com.zalamena.condominios.condominio.domain.condominio.model.AddCondominioForm
import com.zalamena.condominios.condominio.domain.condominio.models.Condominio
import com.zalamena.condominios.condominio.domain.condominio.models.Endereco
import com.zalamena.condominios.condominio.domain.condominio.repository.CondominioRepository

class AddCondominioUseCase(
    private val condominioRepository: CondominioRepository
) {
    suspend operator fun invoke(form: AddCondominioForm): Result<String> {
        val existingCount = condominioRepository.getCondominios()
            .getOrElse { emptyList() }
            .size
        val id = "$existingCount-${form.nome}"
        val condominio = Condominio(
            id = id,
            nome = form.nome,
            endereco = Endereco(
                rua = form.rua,
                numero = form.numero,
                cep = form.cep,
                cidade = form.cidade,
                estado = form.estado
            ),
            apartamentos = emptyList()
        )
        return condominioRepository.addCondominio(condominio).map { id }
    }
}
