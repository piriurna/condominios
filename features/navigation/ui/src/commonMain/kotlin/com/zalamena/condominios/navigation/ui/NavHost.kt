package com.zalamena.condominios.navigation.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.zalamena.condominios.addapartamento.ui.AddApartamentoViewModel
import com.zalamena.condominios.addapartamento.ui.navigation.addApartamentoNavHost
import com.zalamena.condominios.addmorador.ui.AddMoradorOverviewViewModel
import com.zalamena.condominios.addmorador.ui.navigation.AddMorador
import com.zalamena.condominios.addmorador.ui.navigation.addMoradorNavGraph
import com.zalamena.condominios.addpessoa.ui.AddPessoaViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    addApartamentoViewModel: AddApartamentoViewModel,
    addPessoaViewModel: AddPessoaViewModel,
    addMoradorOverviewViewModel: AddMoradorOverviewViewModel
) {
    NavHost(navController, startDestination = AddMorador) {
        addMoradorNavGraph(
            navController,
            addPessoaViewModel = addPessoaViewModel,
            addApartamentoViewModel = addApartamentoViewModel,
            addMoradorOverviewViewModel = addMoradorOverviewViewModel
        )

        addApartamentoNavHost(
            navController,
            addApartamentoViewModel
        )
    }
}