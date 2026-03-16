package com.zalamena.login.ui.createuser.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.zalamena.login.ui.createuser.CreateUserScreen
import com.zalamena.login.ui.createuser.CreateUserViewModel
import kotlinx.serialization.Serializable

@Serializable
object CreateUserRoute

fun NavGraphBuilder.createUserNavHost(
    navController: NavHostController,
    createUserViewModel: CreateUserViewModel
) {
    composable<CreateUserRoute> {
        CreateUserScreen(
            viewModel = createUserViewModel,
            onNavigateBack = { navController.popBackStack() }
        )
    }
}
