package com.calorias.app.data.dao

import androidx.room.*
import com.calorias.app.data.model.Refeicao
import kotlinx.coroutines.flow.Flow

@Dao
interface RefeicaoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserir(refeicao: Refeicao)

    @Update
    suspend fun atualizar(refeicao: Refeicao)

    @Delete
    suspend fun deletar(refeicao: Refeicao)

    @Query("SELECT * FROM refeicoes WHERE data = :data ORDER BY tipo ASC")
    fun listarPorData(data: String): Flow<List<Refeicao>>

    @Query("SELECT * FROM refeicoes ORDER BY data DESC")
    fun listarTodas(): Flow<List<Refeicao>>

    @Query("SELECT SUM(calorias) FROM refeicoes WHERE data = :data")
    fun totalCaloriasDia(data: String): Flow<Int?>

    @Query("SELECT * FROM refeicoes WHERE id = :id")
    suspend fun buscarPorId(id: Int): Refeicao?
}
