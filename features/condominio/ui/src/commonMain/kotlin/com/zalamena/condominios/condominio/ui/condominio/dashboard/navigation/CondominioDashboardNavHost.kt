package com.zalamena.condominios.condominio.ui.condominio.dashboard.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.zalamena.condominios.condominio.ui.addapartamento.AddApartamentoViewModel
import com.zalamena.condominios.condominio.ui.addapartamento.navigation.AddApartamentoRoute
import com.zalamena.condominios.condominio.ui.addmorador.flowController.AddMoradorFlowViewModel
import com.zalamena.condominios.condominio.ui.addmorador.navigation.AddMoradorRoute
import com.zalamena.condominios.condominio.ui.apartamento.detail.ApartamentoDetailScreen
import com.zalamena.condominios.condominio.ui.apartamento.detail.ApartamentoDetailViewModel
import com.zalamena.condominios.condominio.ui.condominio.dashboard.CondominioDashboardScreen
import com.zalamena.condominios.condominio.ui.condominio.dashboard.CondominioDashboardViewModel
import com.zalamena.condominios.condominio.ui.moradores.details.MoradorInfoScreen
import com.zalamena.condominios.condominio.ui.moradores.details.MoradorInfoViewModel
import com.zalamena.condominios.condominio.ui.porteiro.list.PorteiroListScreen
import com.zalamena.condominios.condominio.ui.porteiro.list.PorteiroListViewModel
import com.zalamena.login.ui.createuser.CreateUserViewModel
import com.zalamena.login.ui.createuser.RoleOption
import com.zalamena.login.ui.createuser.navigation.CreateUserRoute
import kotlinx.serialization.Serializable

@Serializable
object CondominioDashboardRoute

@Serializable
object CondominioDashboardScreenRoute

@Serializable
object ApartamentoDetailScreenRoute

@Serializable
object PorteiroListRoute

@Serializable
object AdminMoradorDetailRoute

fun NavGraphBuilder.condominioDashboardNavHost(
    navController: NavController,
    viewModel: CondominioDashboardViewModel,
    addApartamentoViewModel: AddApartamentoViewModel,
    apartamentoDetailViewModel: ApartamentoDetailViewModel,
    addMoradorFlowViewModel: AddMoradorFlowViewModel,
    createUserViewModel: CreateUserViewModel,
    porteiroListViewModel: PorteiroListViewModel,
    moradorInfoViewModel: MoradorInfoViewModel
) {
    navigation<CondominioDashboardRoute>(startDestination = CondominioDashboardScreenRoute) {
        composable<CondominioDashboardScreenRoute> {
            CondominioDashboardScreen(
                viewModel = viewModel,
                onNavigateToAddApartamento = { condominioId ->
                    addApartamentoViewModel.reset()
                    addApartamentoViewModel.setCondominioId(condominioId)
                    navController.navigate(AddApartamentoRoute)
                },
                onNavigateToApartamento = { apartamentoId ->
                    apartamentoDetailViewModel.setApartamentoId(apartamentoId)
                    navController.navigate(ApartamentoDetailScreenRoute)
                },
                onNavigateToCreatePorteiro = { condominioId ->
                    createUserViewModel.reset()
                    createUserViewModel.setRole(RoleOption.PORTEIRO)
                    createUserViewModel.setCondominioId(condominioId)
                    navController.navigate(CreateUserRoute)
                },
                onNavigateToPorteiroList = { condominioId ->
                    porteiroListViewModel.setCondominioId(condominioId)
                    navController.navigate(PorteiroListRoute)
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable<ApartamentoDetailScreenRoute> {
            ApartamentoDetailScreen(
                viewModel = apartamentoDetailViewModel,
                onNavigateToAddMorador = { apartamentoId ->
                    addMoradorFlowViewModel.reset()
                    addMoradorFlowViewModel.setCreatedApartamentoId(apartamentoId)
                    navController.navigate(AddMoradorRoute)
                },
                onNavigateToMoradorDetail = { pessoaId ->
                    moradorInfoViewModel.setPessoaId(pessoaId)
                    navController.navigate(AdminMoradorDetailRoute)
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable<AdminMoradorDetailRoute> {
            moradorInfoViewModel.setAdminMode(true)
            MoradorInfoScreen(
                viewModel = moradorInfoViewModel,
                isAdminMode = true,
                onNavigateToApartamento = { apartamentoId ->
                    apartamentoDetailViewModel.setApartamentoId(apartamentoId)
                    navController.navigate(ApartamentoDetailScreenRoute) {
                        popUpTo(AdminMoradorDetailRoute) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable<PorteiroListRoute> {
            PorteiroListScreen(
                viewModel = porteiroListViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
