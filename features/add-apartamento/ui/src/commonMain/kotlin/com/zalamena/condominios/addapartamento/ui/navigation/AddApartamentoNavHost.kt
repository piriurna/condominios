package com.zalamena.condominios.addapartamento.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.zalamena.condominios.addapartamento.ui.AddApartamentoScreen
import com.zalamena.condominios.addapartamento.ui.AddApartamentoViewModel
import kotlinx.serialization.Serializable

@Serializable
object AddApartamento

fun NavGraphBuilder.addApartamentoNavHost(
    navController: NavController,
    addApartamentoViewModel: AddApartamentoViewModel
) {
    navigation<AddApartamento>(AddApartamento) {
        composable<AddApartamento> {
            AddApartamentoScreen(addApartamentoViewModel) {
                navController.popBackStack() // In this case we are just adding an apartamento and not in the AddMorador Flow
            }
        }
    }
}