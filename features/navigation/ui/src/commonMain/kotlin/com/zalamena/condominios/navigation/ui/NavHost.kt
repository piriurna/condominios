package com.zalamena.condominios.navigation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.zalamena.condominios.addapartamento.ui.AddApartamentoViewModel
import com.zalamena.condominios.addapartamento.ui.navigation.addApartamentoNavHost
import com.zalamena.condominios.addmorador.ui.flowController.AddMoradorFlowViewModel
import com.zalamena.condominios.addmorador.ui.navigation.AddMorador
import com.zalamena.condominios.addmorador.ui.navigation.addMoradorNavGraph
import com.zalamena.condominios.addmorador.ui.overview.AddMoradorOverviewViewModel
import com.zalamena.condominios.addpessoa.ui.AddPessoaViewModel
import kotlinx.serialization.Serializable

@Serializable
object Debug

@Serializable
object Buttons


@Composable
fun AppNavHost(
    navController: NavHostController,
    addApartamentoViewModel: AddApartamentoViewModel,
    addPessoaViewModel: AddPessoaViewModel,
    addMoradorOverviewViewModel: AddMoradorOverviewViewModel,
    addMoradorFlowViewModel: AddMoradorFlowViewModel
) {
    NavHost(navController, startDestination = Debug) {

        navigation<Debug>(startDestination = Buttons) {
            composable<Buttons> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(onClick = { navController.navigate(AddMorador) }) {
                        Text("Go to Add Morador Flow from scratch")
                    }
                    Button(onClick = { navController.navigate(AddMorador) }) {
                        Text("Go to Add Morador Flow with created entities")
                    }
                }
            }
        }

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
    }
}