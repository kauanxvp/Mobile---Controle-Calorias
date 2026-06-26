package com.calorias.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class TipoRefeicao(val label: String) {
    CAFE("Café da Manhã"),
    ALMOCO("Almoço"),
    JANTAR("Jantar"),
    LANCHE("Lanche")
}

@Entity(tableName = "refeicoes")
data class Refeicao(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nomeAlimento: String,
    val calorias: Int,
    val tipo: TipoRefeicao,
    val data: String // formato: "yyyy-MM-dd"
)
