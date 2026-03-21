package com.zalamena.condominios.navigation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.zalamena.condominios.condominio.ui.addapartamento.AddApartamentoViewModel
import com.zalamena.condominios.condominio.ui.addapartamento.navigation.addApartamentoNavHost
import com.zalamena.condominios.condominio.ui.addcondominio.AddCondominioViewModel
import com.zalamena.condominios.condominio.ui.addcondominio.navigation.AddCondominioRoute
import com.zalamena.condominios.condominio.ui.addcondominio.navigation.addCondominioNavHost
import com.zalamena.condominios.condominio.ui.adminhome.AdminHomeScreen
import com.zalamena.condominios.condominio.ui.adminhome.AdminHomeViewModel
import com.zalamena.condominios.condominio.ui.apartamento.detail.ApartamentoDetailViewModel
import com.zalamena.condominios.condominio.ui.addmorador.flowController.AddMoradorFlowViewModel
import com.zalamena.condominios.condominio.ui.addmorador.navigation.AddMoradorRoute
import com.zalamena.condominios.condominio.ui.addmorador.navigation.addMoradorNavGraph
import com.zalamena.condominios.condominio.ui.addmorador.overview.AddMoradorOverviewViewModel
import com.zalamena.condominios.condominio.ui.condominio.dashboard.CondominioDashboardScreen
import com.zalamena.condominios.condominio.ui.condominio.dashboard.CondominioDashboardViewModel
import com.zalamena.condominios.condominio.ui.condominio.dashboard.navigation.CondominioDashboardRoute
import com.zalamena.condominios.condominio.ui.condominio.dashboard.navigation.DoormanApartamentoDetailRoute
import com.zalamena.condominios.condominio.ui.condominio.dashboard.navigation.DoormanMoradorSearchRoute
import com.zalamena.condominios.condominio.ui.condominio.dashboard.navigation.condominioDashboardNavHost
import com.zalamena.condominios.condominio.ui.condominio.dashboard.navigation.doormanApartamentoDetail
import com.zalamena.condominios.condominio.ui.condominio.dashboard.navigation.doormanMoradorSearch
import com.zalamena.condominios.condominio.ui.moradores.search.MoradorSearchViewModel
import com.zalamena.condominios.condominio.ui.porteiro.list.PorteiroListViewModel
import com.zalamena.condominios.pessoa.ui.addpessoa.AddPessoaViewModel
import com.zalamena.login.domain.models.UserRole
import com.zalamena.login.ui.LoginViewModel
import com.zalamena.login.ui.createuser.CreateUserViewModel
import com.zalamena.login.ui.createuser.navigation.CreateUserRoute
import com.zalamena.login.ui.createuser.navigation.createUserNavHost
import com.zalamena.login.ui.navigation.LoginRoute
import com.zalamena.login.ui.navigation.loginNavHost
import kotlinx.serialization.Serializable

@Serializable
object SplashRoute

@Serializable
object DebugRoute

@Serializable
object ButtonsRoute

@Serializable
object AdminHomeRoute

@Serializable
object DoormanHomeRoute


@Composable
fun AppNavHost(
    navController: NavHostController,
    addApartamentoViewModel: AddApartamentoViewModel,
    addPessoaViewModel: AddPessoaViewModel,
    addMoradorOverviewViewModel: AddMoradorOverviewViewModel,
    addMoradorFlowViewModel: AddMoradorFlowViewModel,
    condominioDashboardViewModel: CondominioDashboardViewModel,
    addCondominioViewModel: AddCondominioViewModel,
    apartamentoDetailViewModel: ApartamentoDetailViewModel,
    loginViewModel: LoginViewModel,
    adminHomeViewModel: AdminHomeViewModel,
    createUserViewModel: CreateUserViewModel,
    porteiroListViewModel: PorteiroListViewModel,
    moradorSearchViewModel: MoradorSearchViewModel,
    onLogout: suspend () -> Unit = {}
) {
    NavHost(navController, startDestination = SplashRoute) {

        composable<SplashRoute> {
            Box(modifier = Modifier.fillMaxSize())
        }

        navigation<DebugRoute>(startDestination = ButtonsRoute) {
            composable<ButtonsRoute> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(onClick = { navController.navigate(AdminHomeRoute) }) {
                        Text("Admin Home")
                    }
                    Button(onClick = { navController.navigate(LoginRoute) }) {
                        Text("Login")
                    }
                    Button(onClick = { navController.navigate(CondominioDashboardRoute) }) {
                        Text("Dashboard de Condomínio")
                    }
                    Button(onClick = { navController.navigate(AddCondominioRoute) }) {
                        Text("Adicionar Condomínio")
                    }
                    Button(onClick = { navController.navigate(CreateUserRoute) }) {
                        Text("Criar Usuário")
                    }
                    Button(onClick = { navController.navigate(AddMoradorRoute) }) {
                        Text("Go to Add Morador Flow from scratch")
                    }
                    Button(onClick = { navController.navigate(AddMoradorRoute) }) {
                        Text("Go to Add Morador Flow with created entities")
                    }
                }
            }
        }

        composable<AdminHomeRoute> {
            val coroutineScope = rememberCoroutineScope()
            val lifecycleOwner = LocalLifecycleOwner.current
            DisposableEffect(lifecycleOwner) {
                val observer = LifecycleEventObserver { _, event ->
                    if (event == Lifecycle.Event.ON_RESUME) {
                        adminHomeViewModel.loadCondominios()
                    }
                }
                lifecycleOwner.lifecycle.addObserver(observer)
                onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
            }

            AdminHomeScreen(
                viewModel = adminHomeViewModel,
                onCondominioClick = { condominioId ->
                    condominioDashboardViewModel.setCondominioId(condominioId)
                    condominioDashboardViewModel.setAdminMode(true)
                    navController.navigate(CondominioDashboardRoute)
                },
                onAddCondominioClick = {
                    addCondominioViewModel.reset()
                    navController.navigate(AddCondominioRoute)
                },
                onCreateUserClick = {
                    createUserViewModel.reset()
                    navController.navigate(CreateUserRoute)
                },
                onLogout = {
                    coroutineScope.launch {
                        onLogout()
                        navController.navigate(LoginRoute) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }
            )
        }

        composable<DoormanHomeRoute> {
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

            CondominioDashboardScreen(
                viewModel = condominioDashboardViewModel,
                onNavigateToApartamento = { apartamentoId ->
                    apartamentoDetailViewModel.setApartamentoId(apartamentoId)
                    navController.navigate(DoormanApartamentoDetailRoute)
                },
                onNavigateToSearchMorador = { condominioId ->
                    moradorSearchViewModel.setCondominioId(condominioId)
                    navController.navigate(DoormanMoradorSearchRoute)
                },
                onLogout = {
                    coroutineScope.launch {
                        onLogout()
                        navController.navigate(LoginRoute) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }
            )
        }

        doormanApartamentoDetail(navController, apartamentoDetailViewModel)

        doormanMoradorSearch(navController, moradorSearchViewModel, apartamentoDetailViewModel)

        loginNavHost(
            navController,
            loginViewModel = loginViewModel,
            onLoginSuccess = { role ->
                when (role) {
                    is UserRole.Admin -> {
                        navController.navigate(AdminHomeRoute) {
                            popUpTo(LoginRoute) { inclusive = true }
                        }
                    }
                    is UserRole.Porteiro -> {
                        condominioDashboardViewModel.setCondominioId(role.condominioId)
                        condominioDashboardViewModel.setAdminMode(false)
                        navController.navigate(DoormanHomeRoute) {
                            popUpTo(LoginRoute) { inclusive = true }
                        }
                    }
                }
            }
        )

        addMoradorNavGraph(
            navController,
            addPessoaViewModel = addPessoaViewModel,
            addApartamentoViewModel = addApartamentoViewModel,
            addMoradorOverviewViewModel = addMoradorOverviewViewModel,
            addMoradorFlowViewModel = addMoradorFlowViewModel
        )

        addApartamentoNavHost(
            navController,
            addApartamentoViewModel
        )

        condominioDashboardNavHost(
            navController,
            condominioDashboardViewModel,
            addApartamentoViewModel,
            apartamentoDetailViewModel,
            addMoradorFlowViewModel,
            createUserViewModel,
            porteiroListViewModel
        )

        addCondominioNavHost(
            navController,
            addCondominioViewModel
        )

        createUserNavHost(
            navController,
            createUserViewModel
        )
    }
}
