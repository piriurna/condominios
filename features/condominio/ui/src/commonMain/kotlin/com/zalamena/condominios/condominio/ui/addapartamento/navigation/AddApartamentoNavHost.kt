package com.zalamena.condominios.condominio.ui.addapartamento.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.zalamena.condominios.condominio.ui.addapartamento.AddApartamentoScreen
import com.zalamena.condominios.condominio.ui.addapartamento.AddApartamentoViewModel
import kotlinx.serialization.Serializable

@Serializable
object AddApartamentoRoute

@Serializable
object AddApartamentoScreenRoute

fun NavGraphBuilder.addApartamentoNavHost(
    navController: NavController,
    addApartamentoViewModel: AddApartamentoViewModel
) {
    navigation<AddApartamentoRoute>(AddApartamentoScreenRoute) {
        composable<AddApartamentoScreenRoute> {
            AddApartamentoScreen(
                viewModel = addApartamentoViewModel,
                navigate = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }
    }
}