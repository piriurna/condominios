package com.zalamena.condominios.condominio.ui.condominio.dashboard.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.zalamena.condominios.condominio.ui.addapartamento.AddApartamentoViewModel
import com.zalamena.condominios.condominio.ui.addapartamento.navigation.AddApartamentoRoute
import com.zalamena.condominios.condominio.ui.condominio.dashboard.CondominioDashboardScreen
import com.zalamena.condominios.condominio.ui.condominio.dashboard.CondominioDashboardViewModel
import kotlinx.serialization.Serializable

@Serializable
object CondominioDashboardRoute

@Serializable
object CondominioDashboardScreenRoute

fun NavGraphBuilder.condominioDashboardNavHost(
    navController: NavController,
    viewModel: CondominioDashboardViewModel,
    addApartamentoViewModel: AddApartamentoViewModel
) {
    navigation<CondominioDashboardRoute>(startDestination = CondominioDashboardScreenRoute) {
        composable<CondominioDashboardScreenRoute> {
            CondominioDashboardScreen(
                viewModel = viewModel,
                onNavigateToAddApartamento = { condominioId ->
                    addApartamentoViewModel.setCondominioId(condominioId)
                    navController.navigate(AddApartamentoRoute)
                },
                onNavigateToApartamento = { /* TODO: navigate to apartamento details */ }
            )
        }
    }
}
