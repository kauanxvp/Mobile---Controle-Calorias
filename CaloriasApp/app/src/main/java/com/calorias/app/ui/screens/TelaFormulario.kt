package com.calorias.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.calorias.app.data.model.TipoRefeicao
import com.calorias.app.ui.components.ChipTipoRefeicao
import com.calorias.app.ui.theme.Verde
import com.calorias.app.viewmodel.RefeicaoViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun TelaFormulario(
    viewModel: RefeicaoViewModel,
    onVoltar: () -> Unit
) {
    val nomeAlimento by viewModel.nomeAlimento.collectAsState()
    val calorias by viewModel.calorias.collectAsState()
    val tipoSelecionado by viewModel.tipoSelecionado.collectAsState()
    val erro by viewModel.erroFormulario.collectAsState()
    val emEdicao by viewModel.refeicaoEmEdicao.collectAsState()

    val titulo = if (emEdicao != null) "Editar Refeição" else "Adicionar Refeição"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(titulo, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = androidx.compose.ui.graphics.Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Verde,
                    titleContentColor = androidx.compose.ui.graphics.Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // Campo: nome do alimento
            OutlinedTextField(
                value = nomeAlimento,
                onValueChange = { viewModel.nomeAlimento.value = it },
                label = { Text("Nome do Alimento *") },
                placeholder = { Text("Ex: Arroz com feijão") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = erro != null && nomeAlimento.isBlank()
            )

            // Campo: calorias
            OutlinedTextField(
                value = calorias,
                onValueChange = { viewModel.calorias.value = it.filter { c -> c.isDigit() } },
                label = { Text("Calorias (kcal) *") },
                placeholder = { Text("Ex: 350") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = erro != null && (calorias.isBlank() || calorias.toIntOrNull() == null),
                suffix = { Text("kcal") }
            )

            // Seleção de tipo de refeição
            Text("Tipo de Refeição *", style = MaterialTheme.typography.labelLarge)
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                TipoRefeicao.entries.forEach { tipo ->
                    ChipTipoRefeicao(
                        tipo = tipo,
                        selecionado = tipoSelecionado == tipo,
                        onClick = { viewModel.tipoSelecionado.value = tipo }
                    )
                }
            }

            // Mensagem de erro de validação
            if (erro != null) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = erro!!,
                        modifier = Modifier.padding(12.dp),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Botão salvar
            Button(
                onClick = {
                    val sucesso = viewModel.salvar()
                    if (sucesso) onVoltar()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Verde)
            ) {
                Text(
                    text = if (emEdicao != null) "Salvar Alterações" else "Adicionar Refeição",
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Botão cancelar
            OutlinedButton(
                onClick = onVoltar,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text("Cancelar")
            }
        }
    }
}
