package com.zalamena.condominios.condominio.ui.condominio.dashboard.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.zalamena.condominios.condominio.ui.apartamento.detail.ApartamentoDetailScreen
import com.zalamena.condominios.condominio.ui.apartamento.detail.ApartamentoDetailViewModel
import com.zalamena.condominios.condominio.ui.moradores.search.MoradorSearchScreen
import com.zalamena.condominios.condominio.ui.moradores.search.MoradorSearchViewModel
import kotlinx.serialization.Serializable

@Serializable
object DoormanApartamentoDetailRoute

@Serializable
object DoormanMoradorSearchRoute

fun NavGraphBuilder.doormanApartamentoDetail(
    navController: NavController,
    apartamentoDetailViewModel: ApartamentoDetailViewModel
) {
    composable<DoormanApartamentoDetailRoute> {
        ApartamentoDetailScreen(
            viewModel = apartamentoDetailViewModel,
            isAdminMode = false
        )
    }
}

fun NavGraphBuilder.doormanMoradorSearch(
    navController: NavController,
    moradorSearchViewModel: MoradorSearchViewModel,
    apartamentoDetailViewModel: ApartamentoDetailViewModel
) {
    composable<DoormanMoradorSearchRoute> {
        MoradorSearchScreen(
            viewModel = moradorSearchViewModel,
            onNavigateToApartamento = { apartamentoId ->
                apartamentoDetailViewModel.setApartamentoId(apartamentoId)
                navController.navigate(DoormanApartamentoDetailRoute)
            }
        )
    }
}
