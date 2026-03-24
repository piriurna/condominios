package com.zalamena.condominios.condominio.ui.condominio.dashboard.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.zalamena.condominios.condominio.ui.apartamento.detail.ApartamentoDetailScreen
import com.zalamena.condominios.condominio.ui.apartamento.detail.ApartamentoDetailViewModel
import com.zalamena.condominios.condominio.ui.moradores.details.MoradorInfoScreen
import com.zalamena.condominios.condominio.ui.moradores.details.MoradorInfoViewModel
import com.zalamena.condominios.condominio.ui.moradores.search.MoradorSearchScreen
import com.zalamena.condominios.condominio.ui.moradores.search.MoradorSearchViewModel
import kotlinx.serialization.Serializable

@Serializable
object DoormanApartamentoDetailRoute

@Serializable
object DoormanMoradorSearchRoute

@Serializable
object DoormanMoradorDetailRoute

fun NavGraphBuilder.doormanApartamentoDetail(
    navController: NavController,
    apartamentoDetailViewModel: ApartamentoDetailViewModel,
    moradorInfoViewModel: MoradorInfoViewModel
) {
    composable<DoormanApartamentoDetailRoute> {
        ApartamentoDetailScreen(
            viewModel = apartamentoDetailViewModel,
            isAdminMode = false,
            onNavigateToMoradorDetail = { pessoaId ->
                moradorInfoViewModel.setPessoaId(pessoaId)
                navController.navigate(DoormanMoradorDetailRoute)
            },
            onBack = { navController.popBackStack() }
        )
    }
}

fun NavGraphBuilder.doormanMoradorSearch(
    navController: NavController,
    moradorSearchViewModel: MoradorSearchViewModel,
    moradorInfoViewModel: MoradorInfoViewModel
) {
    composable<DoormanMoradorSearchRoute> {
        MoradorSearchScreen(
            viewModel = moradorSearchViewModel,
            onNavigateToMoradorDetail = { pessoaId ->
                moradorInfoViewModel.setPessoaId(pessoaId)
                navController.navigate(DoormanMoradorDetailRoute)
            },
            onBack = { navController.popBackStack() }
        )
    }
}

fun NavGraphBuilder.doormanMoradorDetail(
    navController: NavController,
    moradorInfoViewModel: MoradorInfoViewModel,
    apartamentoDetailViewModel: ApartamentoDetailViewModel
) {
    composable<DoormanMoradorDetailRoute> {
        MoradorInfoScreen(
            viewModel = moradorInfoViewModel,
            onNavigateToApartamento = { apartamentoId ->
                apartamentoDetailViewModel.setApartamentoId(apartamentoId)
                navController.navigate(DoormanApartamentoDetailRoute) {
                    popUpTo(DoormanMoradorDetailRoute) { inclusive = true }
                }
            },
            onBack = { navController.popBackStack() }
        )
    }
}
