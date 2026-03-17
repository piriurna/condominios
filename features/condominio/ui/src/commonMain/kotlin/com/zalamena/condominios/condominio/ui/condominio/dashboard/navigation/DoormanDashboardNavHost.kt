package com.zalamena.condominios.condominio.ui.condominio.dashboard.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.zalamena.condominios.condominio.ui.apartamento.detail.ApartamentoDetailScreen
import com.zalamena.condominios.condominio.ui.apartamento.detail.ApartamentoDetailViewModel
import kotlinx.serialization.Serializable

@Serializable
object DoormanApartamentoDetailRoute

fun NavGraphBuilder.doormanApartamentoDetail(
    apartamentoDetailViewModel: ApartamentoDetailViewModel
) {
    composable<DoormanApartamentoDetailRoute> {
        ApartamentoDetailScreen(
            viewModel = apartamentoDetailViewModel,
            isAdminMode = false
        )
    }
}
