package com.zalamena.condominios.apartamentos.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.serialization.Serializable


@Serializable
object Apartamento

fun NavGraphBuilder.apartamentosNavHost(
) {
    navigation<Apartamento>(Apartamento) {
        composable<Apartamento> {

        }
    }
}