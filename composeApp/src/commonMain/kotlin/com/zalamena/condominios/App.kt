package com.zalamena.condominios

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import com.zalamena.condominios.condominio.ui.condominio.dashboard.CondominioDashboardViewModel
import com.zalamena.condominios.navigation.ui.AdminHomeRoute
import com.zalamena.condominios.navigation.ui.AppNavHost
import com.zalamena.condominios.navigation.ui.DoormanHomeRoute
import com.zalamena.condominios.navigation.ui.SplashRoute
import com.zalamena.login.domain.usecase.LogoutUseCase
import com.zalamena.login.ui.SplashNavigationEvent
import com.zalamena.login.ui.SplashViewModel
import com.zalamena.login.ui.navigation.LoginRoute
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    MaterialTheme {
        val navController = rememberNavController()
        val splashViewModel: SplashViewModel = koinViewModel()
        val navEvent by splashViewModel.navEvent.collectAsState()
        val condominioDashboardViewModel: CondominioDashboardViewModel = koinViewModel()
        val logoutUseCase: LogoutUseCase = koinInject()

        LaunchedEffect(navEvent) {
            when (val event = navEvent) {
                is SplashNavigationEvent.NavigateToLogin -> {
                    navController.navigate(LoginRoute) {
                        popUpTo(SplashRoute) { inclusive = true }
                    }
                    splashViewModel.onNavigationHandled()
                }
                is SplashNavigationEvent.NavigateToAdminHome -> {
                    navController.navigate(AdminHomeRoute) {
                        popUpTo(SplashRoute) { inclusive = true }
                    }
                    splashViewModel.onNavigationHandled()
                }
                is SplashNavigationEvent.NavigateToDoormanHome -> {
                    condominioDashboardViewModel.setCondominioId(event.condominioId)
                    condominioDashboardViewModel.setAdminMode(false)
                    navController.navigate(DoormanHomeRoute) {
                        popUpTo(SplashRoute) { inclusive = true }
                    }
                    splashViewModel.onNavigationHandled()
                }
                null -> {}
            }
        }

        AppNavHost(
            navController,
            koinViewModel(),
            koinViewModel(),
            koinViewModel(),
            koinViewModel(),
            condominioDashboardViewModel,
            koinViewModel(),
            koinViewModel(),
            koinViewModel(),
            koinViewModel(),
            koinViewModel(),
            koinViewModel(),
            koinViewModel(),
            koinViewModel(),
            onLogout = { logoutUseCase() }
        )
    }
}
