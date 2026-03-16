package com.zalamena.login.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.zalamena.login.ui.LoginScreen
import com.zalamena.login.ui.LoginViewModel
import kotlinx.serialization.Serializable

@Serializable
object LoginRoute

fun NavGraphBuilder.loginNavHost(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    onLoginSuccess: () -> Unit
) {
    composable<LoginRoute> {
        LoginScreen(
            viewModel = loginViewModel,
            onLoginSuccess = onLoginSuccess
        )
    }
}
