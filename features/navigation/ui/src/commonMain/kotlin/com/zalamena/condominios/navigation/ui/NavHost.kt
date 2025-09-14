package com.zalamena.condominios.navigation.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.zalamena.condominios.apartamentos.ui.add.AddApartamentoViewModel
import com.zalamena.condominios.apartamentos.ui.navigation.Apartamento
import com.zalamena.condominios.apartamentos.ui.navigation.apartamentosNavHost
import com.zalamena.condominios.moradores.ui.add.AddMoradorViewModel
import com.zalamena.condominios.moradores.ui.navigation.moradorNavGraph

@Composable
fun AppNavHost(
    navController: NavHostController,
    addApartamentoViewModel: AddApartamentoViewModel,
    addMoradorViewModel: AddMoradorViewModel,
) {
    NavHost(navController, startDestination = Apartamento) {
        apartamentosNavHost(addApartamentoViewModel)

        moradorNavGraph(addMoradorViewModel)
    }
}