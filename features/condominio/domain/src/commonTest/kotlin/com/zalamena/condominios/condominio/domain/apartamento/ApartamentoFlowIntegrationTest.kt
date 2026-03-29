package com.zalamena.condominios.condominio.domain.apartamento

import com.zalamena.condominios.condominio.domain.apartamento.model.AddApartamentoForm
import com.zalamena.condominios.condominio.domain.apartamento.models.Apartamento
import com.zalamena.condominios.condominio.domain.apartamento.repository.ApartamentosRepository
import com.zalamena.condominios.condominio.domain.apartamento.usecase.AddApartamentoUseCase
import com.zalamena.condominios.condominio.domain.condominio.models.Condominio
import com.zalamena.condominios.condominio.domain.condominio.repository.CondominioRepository
import com.zalamena.condominios.condominio.domain.condominio.usecase.GetCondominiosUseCase
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Integration tests that trace the full "add apartamento → see in dashboard" flow.
 *
 * Uses lightweight in-memory fakes that share state, mirroring what the Room
 * @Relation does in production: getCondominios() only returns an apartamento under
 * a condominio if the stored condominioId matches.
 */
class ApartamentoFlowIntegrationTest {

    // Shared storage between both fakes, keyed by condominioId.
    private val apartamentosStorage = mutableListOf<Pair<String, Apartamento>>()

    private val fakeApartamentosRepository = object : ApartamentosRepository {
        override suspend fun getApartamento(apartamentoId: String) =
            Result.failure<Apartamento>(Exception("not used"))

        override suspend fun getApartamentos() =
            Result.success(apartamentosStorage.map { it.second })

        override suspend fun addApartamento(condominioId: String, apartamento: Apartamento): Result<String> =
            runCatching {
                val id = "${apartamentosStorage.size}-${apartamento.numero}"
                apartamentosStorage.add(condominioId to apartamento.copy(id = id))
                id
            }

        override suspend fun getCondominioIdForApartamento(apartamentoId: String): String? =
            apartamentosStorage.find { (_, apt) -> apt.id == apartamentoId }?.first
    }

    private val fakeCondominioRepository = object : CondominioRepository {
        // A single condominio pre-loaded with no apartamentos.
        private val stored = listOf(Condominio.dummy.copy(apartamentos = emptyList()))

        override suspend fun getCondominios() = Result.success(
            // Mirror Room @Relation: only include apartamentos whose condominioId matches.
            stored.map { condo ->
                condo.copy(
                    apartamentos = apartamentosStorage
                        .filter { (storedCondominioId, _) -> storedCondominioId == condo.id }
                        .map { (_, apt) -> apt }
                )
            }
        )

        override suspend fun getCondominio(condominioId: String) =
            Result.failure<Condominio>(Exception("not used"))

        override suspend fun addCondominio(condominio: Condominio) =
            Result.failure<Unit>(Exception("not used"))
    }

    // -------------------------------------------------------------------------
    // Passing baseline — proves the fakes work when condominioId is correct.
    // -------------------------------------------------------------------------

    @Test
    fun `GIVEN condominio WHEN apartamento added with correct condominioId THEN getCondominios includes it`() = runTest {
        val addAptUseCase = AddApartamentoUseCase(fakeApartamentosRepository)
        val getCondominiosUseCase = GetCondominiosUseCase(fakeCondominioRepository)

        addAptUseCase(AddApartamentoForm(condominioId = Condominio.dummy.id, numero = "101", andar = "1"))

        val condominios = getCondominiosUseCase().getOrThrow()
        assertEquals(1, condominios.first().apartamentos.size)
    }

    @Test
    fun `GIVEN empty condominioId WHEN adding apartamento THEN use case returns failure`() = runTest {
        val addAptUseCase = AddApartamentoUseCase(fakeApartamentosRepository)

        val result = addAptUseCase(AddApartamentoForm(condominioId = "", numero = "101", andar = "1"))

        assertTrue(result.isFailure)
    }
}
