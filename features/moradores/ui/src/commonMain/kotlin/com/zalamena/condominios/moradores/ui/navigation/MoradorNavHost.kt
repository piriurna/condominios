package com.zalamena.condominios.moradores.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.zalamena.condominios.moradores.ui.add.AddMoradorScreen
import com.zalamena.condominios.moradores.ui.add.AddMoradorViewModel
import kotlinx.serialization.Serializable


@Serializable
object Morador

@Serializable
object AddMorador

fun NavGraphBuilder.moradorNavGraph(
    addMoradorViewModel: AddMoradorViewModel
) {
    navigation<Morador>(startDestination = AddMorador) {
        composable<AddMorador> {
            AddMoradorScreen(addMoradorViewModel)
        }
    }
}