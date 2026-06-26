package com.calorias.app.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Paleta de cores principal
val Verde = Color(0xFF4CAF50)
val VerdeDark = Color(0xFF388E3C)
val VerdeLight = Color(0xFFA5D6A7)
val Laranja = Color(0xFFFF7043)
val LaranjaLight = Color(0xFFFFCCBC)
val Azul = Color(0xFF42A5F5)
val Roxo = Color(0xFFAB47BC)
val Fundo = Color(0xFFF5F5F5)
val FundoCard = Color(0xFFFFFFFF)
val TextoPrimario = Color(0xFF212121)
val TextoSecundario = Color(0xFF757575)

private val LightColorScheme = lightColorScheme(
    primary = Verde,
    onPrimary = Color.White,
    primaryContainer = VerdeLight,
    secondary = Laranja,
    onSecondary = Color.White,
    background = Fundo,
    surface = FundoCard,
    onBackground = TextoPrimario,
    onSurface = TextoPrimario,
    error = Color(0xFFE53935)
)

@Composable
fun CaloriasTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography(),
        content = content
    )
}

// Cores por tipo de refeição
val corCafe = Color(0xFFFF8F00)
val corAlmoco = Color(0xFF43A047)
val corJantar = Color(0xFF1E88E5)
val corLanche = Color(0xFFAB47BC)
