package com.zalamena.condominios

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.zalamena.condominios.navigation.ui.AppNavHost
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    MaterialTheme {
        val navController = rememberNavController()

        AppNavHost(
            navController,
            koinViewModel(),
            koinViewModel(),
            koinViewModel(),
            koinViewModel(),
            koinViewModel(),
            koinViewModel()
        )
    }
}