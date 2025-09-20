package com.zalamena.condominios.addmorador.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.zalamena.condominios.addapartamento.ui.AddApartamentoScreen
import com.zalamena.condominios.addapartamento.ui.AddApartamentoViewModel
import com.zalamena.condominios.addpessoa.ui.AddPessoaScreen
import com.zalamena.condominios.addpessoa.ui.AddPessoaViewModel
import kotlinx.serialization.Serializable


@Serializable
object AddMorador

@Serializable
object AddMoradorPessoa

@Serializable
object AddMoradorApartamento

@Serializable
object AddMoradorOverview


fun NavGraphBuilder.addMoradorNavGraph(
    navController: NavController,
    addPessoaViewModel: AddPessoaViewModel,
    addApartamentoViewModel: AddApartamentoViewModel
) {
    navigation<AddMorador>(startDestination = AddMoradorPessoa) {
        composable<AddMoradorPessoa> {
            AddPessoaScreen(addPessoaViewModel) {
                navController.navigate(AddMoradorApartamento)
            }
        }

        composable<AddMoradorApartamento> {
            AddApartamentoScreen(addApartamentoViewModel) {
                navController.navigate(AddMoradorOverview)
            }
        }
    }
}