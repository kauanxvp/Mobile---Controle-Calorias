package com.calorias.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.calorias.app.data.model.TipoRefeicao
import com.calorias.app.ui.components.CardRefeicao
import com.calorias.app.ui.components.ProgressoCalorias
import com.calorias.app.ui.components.corDoTipo
import com.calorias.app.ui.theme.*
import com.calorias.app.viewmodel.RefeicaoViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaListagem(
    viewModel: RefeicaoViewModel,
    onAdicionarClick: () -> Unit,
    onEditarClick: () -> Unit
) {
    val refeicoes by viewModel.refeicoesDoDia.collectAsState()
    val totalCalorias by viewModel.totalCalorias.collectAsState()
    val meta by viewModel.metaDiaria.collectAsState()
    val dataAtual by viewModel.dataSelecionada.collectAsState()

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val dataExibicao = runCatching {
        LocalDate.parse(dataAtual, formatter)
            .format(DateTimeFormatter.ofPattern("dd 'de' MMMM", java.util.Locale("pt", "BR")))
    }.getOrDefault(dataAtual)

    val refeicoesPorTipo = viewModel.refeicoesPorTipo(refeicoes)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Contador de Calorias", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Verde,
                    titleContentColor = androidx.compose.ui.graphics.Color.White
                ),
                actions = {
                    IconButton(onClick = { /* navegar para histórico */ }) {
                        Icon(
                            Icons.Default.DateRange,
                            contentDescription = "Histórico",
                            tint = androidx.compose.ui.graphics.Color.White
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.limparFormulario()
                    onAdicionarClick()
                },
                containerColor = Verde
            ) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Refeição",
                    tint = androidx.compose.ui.graphics.Color.White)
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Fundo)
                .padding(padding),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            // Cabeçalho com data e progresso
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Verde)
                        .padding(bottom = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = dataExibicao,
                        color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.9f),
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    ProgressoCalorias(consumido = totalCalorias, meta = meta)
                    Spacer(modifier = Modifier.height(8.dp))
                    val restante = (meta - totalCalorias).coerceAtLeast(0)
                    Text(
                        text = if (totalCalorias <= meta) "$restante kcal restantes"
                               else "${totalCalorias - meta} kcal acima da meta",
                        color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.85f),
                        fontSize = 13.sp
                    )
                }
            }

            // Resumo por tipo de refeição
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    TipoRefeicao.entries.forEach { tipo ->
                        val kcal = refeicoesPorTipo[tipo]?.sumOf { it.calorias } ?: 0
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "$kcal",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = corDoTipo(tipo)
                            )
                            Text(
                                text = tipo.label.split(" ").first(),
                                fontSize = 10.sp,
                                color = TextoSecundario
                            )
                        }
                    }
                }
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            }

            // Lista de refeições agrupadas por tipo
            if (refeicoes.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Nenhuma refeição registrada hoje", color = TextoSecundario)
                            Text("Toque em + para adicionar", fontSize = 13.sp, color = TextoSecundario)
                        }
                    }
                }
            } else {
                TipoRefeicao.entries.forEach { tipo ->
                    val grupo = refeicoesPorTipo[tipo]
                    if (!grupo.isNullOrEmpty()) {
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 16.dp, top = 16.dp, bottom = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = tipo.label,
                                    fontWeight = FontWeight.Bold,
                                    color = corDoTipo(tipo),
                                    fontSize = 13.sp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "${grupo.sumOf { it.calorias }} kcal",
                                    fontSize = 12.sp,
                                    color = TextoSecundario
                                )
                            }
                        }
                        items(grupo, key = { it.id }) { refeicao ->
                            CardRefeicao(
                                refeicao = refeicao,
                                onEditar = {
                                    viewModel.prepararEdicao(it)
                                    onEditarClick()
                                },
                                onDeletar = { viewModel.deletar(it) }
                            )
                        }
                    }
                }
            }
        }
    }
}
