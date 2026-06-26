package com.calorias.app.data.database

import com.calorias.app.data.dao.RefeicaoDao
import com.calorias.app.data.model.Refeicao
import kotlinx.coroutines.flow.Flow

class RefeicaoRepository(private val dao: RefeicaoDao) {

    fun listarPorData(data: String): Flow<List<Refeicao>> = dao.listarPorData(data)

    fun listarTodas(): Flow<List<Refeicao>> = dao.listarTodas()

    fun totalCaloriasDia(data: String): Flow<Int?> = dao.totalCaloriasDia(data)

    suspend fun inserir(refeicao: Refeicao) = dao.inserir(refeicao)

    suspend fun atualizar(refeicao: Refeicao) = dao.atualizar(refeicao)

    suspend fun deletar(refeicao: Refeicao) = dao.deletar(refeicao)

    suspend fun buscarPorId(id: Int): Refeicao? = dao.buscarPorId(id)
}
