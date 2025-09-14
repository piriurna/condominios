package com.zalamena.condominios.addapartamento.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.zalamena.condominios.addapartamento.ui.AddApartamentoScreen
import com.zalamena.condominios.addapartamento.ui.AddApartamentoViewModel
import kotlinx.serialization.Serializable

@Serializable
object AddApartamento

fun NavGraphBuilder.addApartamentoNavHost(
    addApartamentoViewModel: AddApartamentoViewModel
) {
    navigation<AddApartamento>(AddApartamento) {
        composable<AddApartamento> {
            AddApartamentoScreen(addApartamentoViewModel)
        }
    }
}