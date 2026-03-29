package com.zalamena.condominios.navigation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.zalamena.condominios.condominio.ui.addapartamento.AddApartamentoViewModel
import com.zalamena.condominios.condominio.ui.addapartamento.navigation.addApartamentoNavHost
import com.zalamena.condominios.condominio.ui.addcondominio.AddCondominioViewModel
import com.zalamena.condominios.condominio.ui.addcondominio.navigation.addCondominioNavHost
import com.zalamena.condominios.condominio.ui.adminhome.AdminHomeViewModel
import com.zalamena.condominios.condominio.ui.apartamento.detail.ApartamentoDetailViewModel
import com.zalamena.condominios.condominio.ui.addmorador.flowController.AddMoradorFlowViewModel
import com.zalamena.condominios.condominio.ui.addmorador.navigation.addMoradorNavGraph
import com.zalamena.condominios.condominio.ui.addmorador.overview.AddMoradorOverviewViewModel
import com.zalamena.condominios.condominio.ui.addmorador.searchpessoa.SearchPessoaViewModel
import com.zalamena.condominios.condominio.ui.condominio.dashboard.CondominioDashboardViewModel
import com.zalamena.condominios.condominio.ui.condominio.dashboard.navigation.condominioDashboardNavHost
import com.zalamena.condominios.condominio.ui.moradores.details.MoradorInfoViewModel
import com.zalamena.condominios.condominio.ui.moradores.search.MoradorSearchViewModel
import com.zalamena.condominios.condominio.ui.porteiro.list.PorteiroListViewModel
import com.zalamena.condominios.navigation.ui.shell.AdminShellScreen
import com.zalamena.condominios.navigation.ui.shell.DoormanShellScreen
import com.zalamena.login.ui.LoginViewModel
import com.zalamena.login.ui.createuser.CreateUserViewModel
import com.zalamena.login.ui.createuser.navigation.createUserNavHost
import com.zalamena.login.ui.navigation.loginNavHost
import com.zalamena.login.domain.models.UserRole
import com.zalamena.login.ui.navigation.LoginRoute
import com.zalamena.condominios.pessoa.ui.addpessoa.AddPessoaViewModel
import kotlinx.serialization.Serializable

@Serializable
object SplashRoute

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
    moradorInfoViewModel: MoradorInfoViewModel,
    searchPessoaViewModel: SearchPessoaViewModel,
    onLogout: suspend () -> Unit = {}
) {
    NavHost(navController, startDestination = SplashRoute) {

        composable<SplashRoute> {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationCity,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Condominios",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Gestao predial",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Verificando sessao...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        composable<AdminHomeRoute> {
            AdminShellScreen(
                parentNavController = navController,
                adminHomeViewModel = adminHomeViewModel,
                condominioDashboardViewModel = condominioDashboardViewModel,
                addCondominioViewModel = addCondominioViewModel,
                createUserViewModel = createUserViewModel,
                onLogout = onLogout
            )
        }

        composable<DoormanHomeRoute> {
            DoormanShellScreen(
                parentNavController = navController,
                condominioDashboardViewModel = condominioDashboardViewModel,
                moradorSearchViewModel = moradorSearchViewModel,
                apartamentoDetailViewModel = apartamentoDetailViewModel,
                moradorInfoViewModel = moradorInfoViewModel,
                onLogout = onLogout
            )
        }

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
                    is UserRole.Morador -> {
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
            addMoradorFlowViewModel = addMoradorFlowViewModel,
            searchPessoaViewModel = searchPessoaViewModel
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
            porteiroListViewModel,
            moradorInfoViewModel
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
