package com.zalamena.condominios.navigation.ui.shell

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import com.zalamena.condominios.condominio.ui.addcondominio.AddCondominioViewModel
import com.zalamena.condominios.condominio.ui.addcondominio.navigation.AddCondominioRoute
import com.zalamena.condominios.condominio.ui.adminhome.AdminHomeScreen
import com.zalamena.condominios.condominio.ui.adminhome.AdminHomeViewModel
import com.zalamena.condominios.condominio.ui.condominio.dashboard.CondominioDashboardViewModel
import com.zalamena.condominios.condominio.ui.condominio.dashboard.navigation.CondominioDashboardRoute
import com.zalamena.login.ui.createuser.CreateUserScreen
import com.zalamena.login.ui.createuser.CreateUserViewModel
import com.zalamena.login.ui.navigation.LoginRoute
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
object AdminCondominiosTab

@Serializable
object AdminUsersTab

@Composable
fun AdminShellScreen(
    parentNavController: NavController,
    adminHomeViewModel: AdminHomeViewModel,
    condominioDashboardViewModel: CondominioDashboardViewModel,
    addCondominioViewModel: AddCondominioViewModel,
    createUserViewModel: CreateUserViewModel,
    onLogout: suspend () -> Unit
) {
    val tabNavController = rememberNavController()
    val navBackStackEntry = tabNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route
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

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Condominios") },
                    label = { Text("Condominios") },
                    selected = currentRoute?.contains("AdminCondominiosTab") == true,
                    onClick = {
                        tabNavController.navigate(AdminCondominiosTab) {
                            popUpTo(tabNavController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Usuarios") },
                    label = { Text("Usuarios") },
                    selected = currentRoute?.contains("AdminUsersTab") == true,
                    onClick = {
                        createUserViewModel.reset()
                        tabNavController.navigate(AdminUsersTab) {
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
            startDestination = AdminCondominiosTab,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<AdminCondominiosTab> {
                AdminHomeScreen(
                    viewModel = adminHomeViewModel,
                    onCondominioClick = { condominioId ->
                        condominioDashboardViewModel.setCondominioId(condominioId)
                        condominioDashboardViewModel.setAdminMode(true)
                        parentNavController.navigate(CondominioDashboardRoute)
                    },
                    onAddCondominioClick = {
                        addCondominioViewModel.reset()
                        parentNavController.navigate(AddCondominioRoute)
                    },
                    onCreateUserClick = {
                        createUserViewModel.reset()
                        tabNavController.navigate(AdminUsersTab) {
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

            composable<AdminUsersTab> {
                CreateUserScreen(
                    viewModel = createUserViewModel,
                    onNavigateBack = {
                        tabNavController.navigate(AdminCondominiosTab) {
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
    }
}
