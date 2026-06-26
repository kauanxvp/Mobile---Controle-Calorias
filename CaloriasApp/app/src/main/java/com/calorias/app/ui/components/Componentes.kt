package com.calorias.app.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.calorias.app.data.model.Refeicao
import com.calorias.app.data.model.TipoRefeicao
import com.calorias.app.ui.theme.*

// Retorna a cor associada ao tipo de refeição
fun corDoTipo(tipo: TipoRefeicao): Color = when (tipo) {
    TipoRefeicao.CAFE   -> corCafe
    TipoRefeicao.ALMOCO -> corAlmoco
    TipoRefeicao.JANTAR -> corJantar
    TipoRefeicao.LANCHE -> corLanche
}

// Card de refeição reutilizável
@Composable
fun CardRefeicao(
    refeicao: Refeicao,
    onEditar: (Refeicao) -> Unit,
    onDeletar: (Refeicao) -> Unit
) {
    var mostrarConfirmacao by remember { mutableStateOf(false) }
    val cor = corDoTipo(refeicao.tipo)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = FundoCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Indicador colorido do tipo
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(cor)
            )
            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = refeicao.nomeAlimento,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = TextoPrimario
                )
                Text(
                    text = refeicao.tipo.label,
                    fontSize = 12.sp,
                    color = cor
                )
            }

            Text(
                text = "${refeicao.calorias} kcal",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = TextoPrimario
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(onClick = { onEditar(refeicao) }) {
                Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Azul)
            }
            IconButton(onClick = { mostrarConfirmacao = true }) {
                Icon(Icons.Default.Delete, contentDescription = "Deletar", tint = Laranja)
            }
        }
    }

    // Diálogo de confirmação de exclusão
    if (mostrarConfirmacao) {
        AlertDialog(
            onDismissRequest = { mostrarConfirmacao = false },
            title = { Text("Excluir refeição?") },
            text = { Text("Deseja remover \"${refeicao.nomeAlimento}\" do seu registro?") },
            confirmButton = {
                TextButton(onClick = {
                    onDeletar(refeicao)
                    mostrarConfirmacao = false
                }) {
                    Text("Excluir", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarConfirmacao = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

// Barra de progresso circular de calorias
@Composable
fun ProgressoCalorias(
    consumido: Int,
    meta: Int,
    modifier: Modifier = Modifier
) {
    val progresso = (consumido.toFloat() / meta.toFloat()).coerceIn(0f, 1f)
    val progressoAnimado by animateFloatAsState(
        targetValue = progresso,
        animationSpec = tween(durationMillis = 800),
        label = "progresso"
    )
    val cor = when {
        progresso < 0.6f -> Verde
        progresso < 0.9f -> Color(0xFFFFA726)
        else             -> Laranja
    }

    Box(contentAlignment = Alignment.Center, modifier = modifier.size(160.dp)) {
        CircularProgressIndicator(
            progress = { 1f },
            modifier = Modifier.fillMaxSize(),
            color = VerdeLight,
            strokeWidth = 12.dp
        )
        CircularProgressIndicator(
            progress = { progressoAnimado },
            modifier = Modifier.fillMaxSize(),
            color = cor,
            strokeWidth = 12.dp
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$consumido",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = TextoPrimario
            )
            Text(text = "de $meta kcal", fontSize = 11.sp, color = TextoSecundario)
        }
    }
}

// Chip de seleção de tipo de refeição
@Composable
fun ChipTipoRefeicao(
    tipo: TipoRefeicao,
    selecionado: Boolean,
    onClick: () -> Unit
) {
    val cor = corDoTipo(tipo)
    FilterChip(
        selected = selecionado,
        onClick = onClick,
        label = { Text(tipo.label, fontSize = 12.sp) },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = cor.copy(alpha = 0.2f),
            selectedLabelColor = cor
        )
    )
}
