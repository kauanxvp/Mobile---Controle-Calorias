package com.calorias.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.calorias.app.data.model.Refeicao
import com.calorias.app.data.model.TipoRefeicao
import com.calorias.app.ui.components.corDoTipo
import com.calorias.app.ui.theme.*
import com.calorias.app.viewmodel.RefeicaoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaDetalhes(
    viewModel: RefeicaoViewModel,
    onVoltar: () -> Unit,
    onEditar: () -> Unit
) {
    val refeicaoEmEdicao by viewModel.refeicaoEmEdicao.collectAsState()
    val refeicoes by viewModel.refeicoesDoDia.collectAsState()
    val total by viewModel.totalCalorias.collectAsState()
    val meta by viewModel.metaDiaria.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalhes do Dia", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Verde,
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Fundo)
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Card de resumo geral
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = FundoCard),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Resumo do Dia", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            InfoItem("Total", "$total kcal", Verde)
                            InfoItem("Meta", "$meta kcal", Azul)
                            InfoItem(
                                "Saldo",
                                "${meta - total} kcal",
                                if (total <= meta) Verde else Laranja
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { (total.toFloat() / meta).coerceIn(0f, 1f) },
                            modifier = Modifier.fillMaxWidth().height(8.dp),
                            color = if (total <= meta) Verde else Laranja,
                            trackColor = VerdeLight
                        )
                    }
                }
            }

            // Detalhe por tipo de refeição
            item {
                Text("Por Refeição", fontWeight = FontWeight.Bold, fontSize = 15.sp,
                    modifier = Modifier.padding(top = 4.dp))
            }

            TipoRefeicao.entries.forEach { tipo ->
                val grupo = refeicoes.filter { it.tipo == tipo }
                if (grupo.isNotEmpty()) {
                    item {
                        DetalheGrupo(tipo = tipo, itens = grupo)
                    }
                }
            }

            if (refeicoes.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Nenhuma refeição registrada.", color = TextoSecundario)
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoItem(label: String, valor: String, cor: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(valor, fontWeight = FontWeight.Bold, color = cor, fontSize = 16.sp)
        Text(label, fontSize = 12.sp, color = TextoSecundario)
    }
}

@Composable
private fun DetalheGrupo(tipo: TipoRefeicao, itens: List<Refeicao>) {
    val cor = corDoTipo(tipo)
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = FundoCard),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(tipo.label, fontWeight = FontWeight.SemiBold, color = cor)
                Text(
                    "${itens.sumOf { it.calorias }} kcal",
                    fontWeight = FontWeight.Bold,
                    color = cor
                )
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp), color = cor.copy(alpha = 0.2f))
            itens.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(item.nomeAlimento, fontSize = 13.sp, color = TextoPrimario,
                        modifier = Modifier.weight(1f))
                    Text("${item.calorias} kcal", fontSize = 13.sp, color = TextoSecundario)
                }
            }
        }
    }
}
