package com.calorias.app.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.calorias.app.data.database.CaloriasDatabase
import com.calorias.app.data.database.RefeicaoRepository
import com.calorias.app.data.model.Refeicao
import com.calorias.app.data.model.TipoRefeicao
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class RefeicaoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: RefeicaoRepository
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    // Data selecionada (padrão = hoje)
    private val _dataSelecionada = MutableStateFlow(LocalDate.now().format(formatter))
    val dataSelecionada: StateFlow<String> = _dataSelecionada.asStateFlow()

    // Refeições do dia selecionado
    val refeicoesDoDia: StateFlow<List<Refeicao>> = _dataSelecionada
        .flatMapLatest { data -> repository.listarPorData(data) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Total de calorias do dia
    val totalCalorias: StateFlow<Int> = refeicoesDoDia
        .map { lista -> lista.sumOf { it.calorias } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Meta diária de calorias
    val metaDiaria = MutableStateFlow(2000)

    // Estado do formulário
    val nomeAlimento = MutableStateFlow("")
    val calorias = MutableStateFlow("")
    val tipoSelecionado = MutableStateFlow(TipoRefeicao.CAFE)

    // Refeição em edição
    private val _refeicaoEmEdicao = MutableStateFlow<Refeicao?>(null)
    val refeicaoEmEdicao: StateFlow<Refeicao?> = _refeicaoEmEdicao.asStateFlow()

    // Mensagem de erro/validação
    val erroFormulario = MutableStateFlow<String?>(null)

    init {
        val dao = CaloriasDatabase.getDatabase(application).refeicaoDao()
        repository = RefeicaoRepository(dao)
    }

    fun selecionarData(data: String) {
        _dataSelecionada.value = data
    }

    fun prepararEdicao(refeicao: Refeicao) {
        _refeicaoEmEdicao.value = refeicao
        nomeAlimento.value = refeicao.nomeAlimento
        calorias.value = refeicao.calorias.toString()
        tipoSelecionado.value = refeicao.tipo
    }

    fun limparFormulario() {
        _refeicaoEmEdicao.value = null
        nomeAlimento.value = ""
        calorias.value = ""
        tipoSelecionado.value = TipoRefeicao.CAFE
        erroFormulario.value = null
    }

    fun salvar(): Boolean {
        // Validações básicas — AJUSTADO MANUALMENTE
        if (nomeAlimento.value.isBlank()) {
            erroFormulario.value = "O nome do alimento é obrigatório."
            return false
        }
        val caloriasInt = calorias.value.toIntOrNull()
        if (caloriasInt == null || caloriasInt <= 0) {
            erroFormulario.value = "Informe um valor de calorias válido (número positivo)."
            return false
        }
        erroFormulario.value = null

        viewModelScope.launch {
            val edicao = _refeicaoEmEdicao.value
            if (edicao != null) {
                repository.atualizar(
                    edicao.copy(
                        nomeAlimento = nomeAlimento.value.trim(),
                        calorias = caloriasInt,
                        tipo = tipoSelecionado.value
                    )
                )
            } else {
                repository.inserir(
                    Refeicao(
                        nomeAlimento = nomeAlimento.value.trim(),
                        calorias = caloriasInt,
                        tipo = tipoSelecionado.value,
                        data = _dataSelecionada.value
                    )
                )
            }
        }
        limparFormulario()
        return true
    }

    fun deletar(refeicao: Refeicao) {
        viewModelScope.launch {
            repository.deletar(refeicao)
        }
    }

    // Agrupa refeições por tipo para exibição
    fun refeicoesPorTipo(lista: List<Refeicao>): Map<TipoRefeicao, List<Refeicao>> {
        return lista.groupBy { it.tipo }
    }
}

class RefeicaoViewModelFactory(private val application: Application) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RefeicaoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RefeicaoViewModel(application) as T
        }
        throw IllegalArgumentException("ViewModel desconhecido")
    }
}
