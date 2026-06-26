package com.calorias.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.calorias.app.ui.screens.TelaDetalhes
import com.calorias.app.ui.screens.TelaFormulario
import com.calorias.app.ui.screens.TelaListagem
import com.calorias.app.ui.theme.CaloriasTheme
import com.calorias.app.viewmodel.RefeicaoViewModel
import com.calorias.app.viewmodel.RefeicaoViewModelFactory

// Rotas de navegação
object Rotas {
    const val LISTAGEM  = "listagem"
    const val FORMULARIO = "formulario"
    const val DETALHES  = "detalhes"
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CaloriasTheme {
                CaloriasApp()
            }
        }
    }
}

@Composable
fun CaloriasApp() {
    val navController: NavHostController = rememberNavController()
    val viewModel: RefeicaoViewModel = viewModel(
        factory = RefeicaoViewModelFactory(
            androidx.compose.ui.platform.LocalContext.current.applicationContext
                as android.app.Application
        )
    )

    NavHost(navController = navController, startDestination = Rotas.LISTAGEM) {

        composable(Rotas.LISTAGEM) {
            TelaListagem(
                viewModel = viewModel,
                onAdicionarClick = { navController.navigate(Rotas.FORMULARIO) },
                onEditarClick = { navController.navigate(Rotas.FORMULARIO) }
            )
        }

        composable(Rotas.FORMULARIO) {
            TelaFormulario(
                viewModel = viewModel,
                onVoltar = { navController.popBackStack() }
            )
        }

        composable(Rotas.DETALHES) {
            TelaDetalhes(
                viewModel = viewModel,
                onVoltar = { navController.popBackStack() },
                onEditar = { navController.navigate(Rotas.FORMULARIO) }
            )
        }
    }
}
