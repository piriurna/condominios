package com.zalamena.condominios.addmorador.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.zalamena.condominios.addapartamento.ui.AddApartamentoScreen
import com.zalamena.condominios.addapartamento.ui.AddApartamentoViewModel
import com.zalamena.condominios.addmorador.ui.AddMoradorOverviewScreen
import com.zalamena.condominios.addmorador.ui.AddMoradorOverviewViewModel
import com.zalamena.condominios.addpessoa.ui.AddPessoaScreen
import com.zalamena.condominios.addpessoa.ui.AddPessoaViewModel
import kotlinx.serialization.Serializable


@Serializable
object AddMorador

@Serializable
object AddMoradorPessoa

@Serializable
data class AddMoradorApartamento(
    val pessoaId: String
)

@Serializable
data class AddMoradorOverview(
    val pessoaId: String?,
    val apartamentoId: String?
)


fun NavGraphBuilder.addMoradorNavGraph( // TODO: Improve this flow to make it less ui dependant and have these decisions on domain
    navController: NavController,
    addPessoaViewModel: AddPessoaViewModel,
    addApartamentoViewModel: AddApartamentoViewModel,
    addMoradorOverviewViewModel: AddMoradorOverviewViewModel
) {
    navigation<AddMorador>(startDestination = AddMoradorPessoa) {
        composable<AddMoradorPessoa> {
            AddPessoaScreen(addPessoaViewModel) {
                navController.navigate(AddMoradorApartamento(addPessoaViewModel.uiState.value.createdPessoaId!!))
            }
        }

        composable<AddMoradorApartamento> {
            AddApartamentoScreen(addApartamentoViewModel) {
                val apartamento: AddMoradorApartamento = it.toRoute()
                navController.navigate(AddMoradorOverview(
                    apartamento.pessoaId,
                    addApartamentoViewModel.uiState.value.createdApartamentoId!!
                ))
            }
        }

        composable<AddMoradorOverview> {
            val overview: AddMoradorOverview = it.toRoute()
            AddMoradorOverviewScreen(
                addMoradorOverviewViewModel,
                overview.pessoaId,
                overview.apartamentoId
            ) {
                navController.popBackStack()
            }
        }
    }
}