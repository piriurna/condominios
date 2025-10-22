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
            AddApartamentoScreen(addApartamentoViewModel) {
                navController.popBackStack() // In this case we are just adding an apartamento and not in the AddMorador Flow
            }
        }
    }
}