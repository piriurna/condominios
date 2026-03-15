package com.zalamena.condominios.condominio.ui.addcondominio.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.zalamena.condominios.condominio.ui.addcondominio.AddCondominioScreen
import com.zalamena.condominios.condominio.ui.addcondominio.AddCondominioViewModel
import kotlinx.serialization.Serializable

@Serializable
object AddCondominioRoute

@Serializable
object AddCondominioScreenRoute

fun NavGraphBuilder.addCondominioNavHost(
    navController: NavController,
    viewModel: AddCondominioViewModel
) {
    navigation<AddCondominioRoute>(startDestination = AddCondominioScreenRoute) {
        composable<AddCondominioScreenRoute> {
            AddCondominioScreen(
                viewModel = viewModel,
                navigate = { navController.popBackStack() }
            )
        }
    }
}
