package com.zalamena.condominios.navigation.ui.shell

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.zalamena.condominios.condominio.ui.apartamento.detail.ApartamentoDetailViewModel
import com.zalamena.condominios.condominio.ui.condominio.dashboard.CondominioDashboardScreen
import com.zalamena.condominios.condominio.ui.condominio.dashboard.CondominioDashboardViewModel
import com.zalamena.condominios.condominio.ui.condominio.dashboard.navigation.DoormanApartamentoDetailRoute
import com.zalamena.condominios.condominio.ui.condominio.dashboard.navigation.DoormanMoradorDetailRoute
import com.zalamena.condominios.condominio.ui.condominio.dashboard.navigation.DoormanMoradorSearchRoute
import com.zalamena.condominios.condominio.ui.moradores.details.MoradorInfoScreen
import com.zalamena.condominios.condominio.ui.moradores.details.MoradorInfoViewModel
import com.zalamena.condominios.condominio.ui.moradores.search.MoradorSearchScreen
import com.zalamena.condominios.condominio.ui.moradores.search.MoradorSearchViewModel
import com.zalamena.condominios.condominio.ui.apartamento.detail.ApartamentoDetailScreen
import com.zalamena.login.ui.navigation.LoginRoute
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
object DoormanHomeTab

@Serializable
object DoormanSearchTab

@Composable
fun DoormanShellScreen(
    parentNavController: NavController,
    condominioDashboardViewModel: CondominioDashboardViewModel,
    moradorSearchViewModel: MoradorSearchViewModel,
    apartamentoDetailViewModel: ApartamentoDetailViewModel,
    moradorInfoViewModel: MoradorInfoViewModel,
    onLogout: suspend () -> Unit
) {
    val tabNavController = rememberNavController()
    val navBackStackEntry by tabNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val coroutineScope = rememberCoroutineScope()

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                condominioDashboardViewModel.loadCondominios()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    val condominioId = condominioDashboardViewModel.uiState.collectAsState().value.condominioId

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                    label = { Text("Inicio") },
                    selected = currentRoute?.contains("DoormanHomeTab") == true,
                    onClick = {
                        tabNavController.navigate(DoormanHomeTab) {
                            popUpTo(tabNavController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
                    label = { Text("Buscar") },
                    selected = currentRoute?.contains("DoormanSearchTab") == true,
                    onClick = {
                        moradorSearchViewModel.setCondominioId(condominioId)
                        tabNavController.navigate(DoormanSearchTab) {
                            popUpTo(tabNavController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = tabNavController,
            startDestination = DoormanHomeTab,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<DoormanHomeTab> {
                CondominioDashboardScreen(
                    viewModel = condominioDashboardViewModel,
                    onNavigateToApartamento = { apartamentoId ->
                        apartamentoDetailViewModel.setApartamentoId(apartamentoId)
                        tabNavController.navigate(DoormanApartamentoDetailRoute)
                    },
                    onNavigateToSearchMorador = { _ ->
                        moradorSearchViewModel.setCondominioId(condominioId)
                        tabNavController.navigate(DoormanSearchTab) {
                            popUpTo(tabNavController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onLogout = {
                        coroutineScope.launch {
                            onLogout()
                            parentNavController.navigate(LoginRoute) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    }
                )
            }

            composable<DoormanSearchTab> {
                MoradorSearchScreen(
                    viewModel = moradorSearchViewModel,
                    onNavigateToMoradorDetail = { pessoaId ->
                        moradorInfoViewModel.setPessoaId(pessoaId)
                        tabNavController.navigate(DoormanMoradorDetailRoute)
                    },
                    onBack = {
                        tabNavController.navigate(DoormanHomeTab) {
                            popUpTo(tabNavController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }

            composable<DoormanApartamentoDetailRoute> {
                ApartamentoDetailScreen(
                    viewModel = apartamentoDetailViewModel,
                    isAdminMode = false,
                    onNavigateToMoradorDetail = { pessoaId ->
                        moradorInfoViewModel.setPessoaId(pessoaId)
                        tabNavController.navigate(DoormanMoradorDetailRoute)
                    },
                    onBack = { tabNavController.popBackStack() }
                )
            }

            composable<DoormanMoradorDetailRoute> {
                MoradorInfoScreen(
                    viewModel = moradorInfoViewModel,
                    onNavigateToApartamento = { apartamentoId ->
                        apartamentoDetailViewModel.setApartamentoId(apartamentoId)
                        tabNavController.navigate(DoormanApartamentoDetailRoute) {
                            popUpTo(DoormanMoradorDetailRoute) { inclusive = true }
                        }
                    },
                    onBack = { tabNavController.popBackStack() }
                )
            }
        }
    }
}
