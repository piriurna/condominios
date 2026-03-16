package com.zalamena.condominios.condominio.ui.addmorador.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.zalamena.condominios.condominio.ui.addapartamento.AddApartamentoScreen
import com.zalamena.condominios.condominio.ui.addapartamento.AddApartamentoViewModel
import com.zalamena.condominios.condominio.ui.addmorador.flowController.AddMoradorFlowViewModel
import com.zalamena.condominios.condominio.ui.addmorador.overview.AddMoradorOverviewScreen
import com.zalamena.condominios.condominio.ui.addmorador.overview.AddMoradorOverviewViewModel
import com.zalamena.condominios.pessoa.ui.addpessoa.AddPessoaScreen
import com.zalamena.condominios.pessoa.ui.addpessoa.AddPessoaViewModel
import kotlinx.serialization.Serializable


@Serializable
object AddMoradorRoute

@Serializable
object AddMoradorPessoaRoute

@Serializable
object AddMoradorApartamentoRoute

@Serializable
object AddMoradorOverviewRoute


fun NavGraphBuilder.addMoradorNavGraph( // TODO: Improve this flow to make it less ui dependant and have these decisions on domain
    navController: NavController,
    addPessoaViewModel: AddPessoaViewModel,
    addApartamentoViewModel: AddApartamentoViewModel,
    addMoradorOverviewViewModel: AddMoradorOverviewViewModel,
    addMoradorFlowViewModel: AddMoradorFlowViewModel
) {
    navigation<AddMoradorRoute>(startDestination = AddMoradorPessoaRoute) {
        composable<AddMoradorPessoaRoute> {
            AddPessoaScreen(addPessoaViewModel) {
                addMoradorFlowViewModel.setCreatedPessoaId(addPessoaViewModel.uiState.value.createdPessoaId!!)
                if (addMoradorFlowViewModel.uiState.value.createdApartamentoId != null) {
                    navController.navigate(AddMoradorOverviewRoute)
                } else {
                    navController.navigate(AddMoradorApartamentoRoute)
                }
            }
        }

        composable<AddMoradorApartamentoRoute> {
            AddApartamentoScreen(addApartamentoViewModel) {
                addMoradorFlowViewModel.setCreatedApartamentoId(addApartamentoViewModel.uiState.value.createdApartamentoId!!)
                navController.navigate(AddMoradorOverviewRoute)
            }
        }

        composable<AddMoradorOverviewRoute> {
            val pessoaId = addMoradorFlowViewModel.uiState.value.createdPessoaId
            val apartamentoId = addMoradorFlowViewModel.uiState.value.createdApartamentoId
            AddMoradorOverviewScreen(
                addMoradorOverviewViewModel,
                pessoaId,
                apartamentoId
            ) {
                navController.popBackStack(AddMoradorRoute, inclusive = true)
            }
        }
    }
}