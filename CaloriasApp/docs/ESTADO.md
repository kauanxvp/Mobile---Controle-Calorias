# Plano de Estado – Contador de Calorias

## Tabela de Estado

| Dado | Tipo | Local no código | Como é atualizado |
|---|---|---|---|
| Lista de refeições do dia | `StateFlow<List<Refeicao>>` | `RefeicaoViewModel.refeicoesDoDia` | Flow do Room, reage automaticamente ao banco |
| Total de calorias do dia | `StateFlow<Int>` | `RefeicaoViewModel.totalCalorias` | Derivado de `refeicoesDoDia` via `.map { sumOf }` |
| Data selecionada | `MutableStateFlow<String>` | `RefeicaoViewModel._dataSelecionada` | Ao usuário mudar a data |
| Meta diária de calorias | `MutableStateFlow<Int>` | `RefeicaoViewModel.metaDiaria` | Valor padrão 2000; editável futuramente |
| Campo: nome do alimento | `MutableStateFlow<String>` | `RefeicaoViewModel.nomeAlimento` | `onValueChange` do OutlinedTextField |
| Campo: calorias | `MutableStateFlow<String>` | `RefeicaoViewModel.calorias` | `onValueChange` do OutlinedTextField |
| Tipo de refeição selecionado | `MutableStateFlow<TipoRefeicao>` | `RefeicaoViewModel.tipoSelecionado` | `onClick` do ChipTipoRefeicao |
| Refeição em edição | `StateFlow<Refeicao?>` | `RefeicaoViewModel.refeicaoEmEdicao` | `prepararEdicao()` / `limparFormulario()` |
| Mensagem de erro | `MutableStateFlow<String?>` | `RefeicaoViewModel.erroFormulario` | Função `salvar()` após validação |

## Fluxo de Dados

```
Room Database (SQLite)
    ↓  Flow<List<Refeicao>>
RefeicaoDao.listarPorData()
    ↓
RefeicaoRepository
    ↓
RefeicaoViewModel
  refeicoesDoDia (StateFlow) → TelaListagem → LazyColumn → CardRefeicao
  totalCalorias (StateFlow)  → TelaListagem → ProgressoCalorias
  nomeAlimento  (StateFlow)  → TelaFormulario → OutlinedTextField
  calorias      (StateFlow)  → TelaFormulario → OutlinedTextField
  tipoSelecionado(StateFlow) → TelaFormulario → ChipTipoRefeicao
  erroFormulario (StateFlow) → TelaFormulario → Card de erro
```

## Persistência

- **Banco:** SQLite via Room (`calorias_database`)
- **Tabela:** `refeicoes` com colunas: `id`, `nomeAlimento`, `calorias`, `tipo`, `data`
- **Garantia:** Os dados persistem entre sessões pois ficam armazenados no armazenamento interno do dispositivo.
- **Verificação:** Fechar e reabrir o app mantém todos os registros intactos.
