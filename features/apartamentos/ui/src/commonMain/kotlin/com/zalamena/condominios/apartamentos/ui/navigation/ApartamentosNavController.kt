package com.zalamena.condominios.apartamentos.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.zalamena.condominios.apartamentos.ui.add.AddApartamentoScreen
import com.zalamena.condominios.apartamentos.ui.add.AddApartamentoViewModel
import kotlinx.serialization.Serializable


@Serializable
object Apartamento
@Serializable
object AddApartamento

fun NavGraphBuilder.apartamentosNavHost(
    addApartamentoViewModel: AddApartamentoViewModel
) {
    navigation<Apartamento>(AddApartamento) {
        composable<AddApartamento> {
            AddApartamentoScreen(addApartamentoViewModel)
        }
    }
}