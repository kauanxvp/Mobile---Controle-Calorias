# Mapeamento de Componentes – Contador de Calorias

## Hierarquia de Componentes

```
CaloriasApp (NavHost)
│
├── TelaListagem
│   ├── TopAppBar
│   │   └── IconButton (Histórico)
│   ├── Header com progresso
│   │   ├── ProgressoCalorias (componente reutilizável)  ← circular
│   │   └── Texto de calorias restantes
│   ├── Resumo por tipo (Row)
│   │   └── 4x coluna (Café / Almoço / Jantar / Lanche)
│   ├── LazyColumn de refeições
│   │   ├── Cabeçalho de grupo (por TipoRefeicao)
│   │   └── CardRefeicao (componente reutilizável)
│   │       ├── Indicador colorido do tipo
│   │       ├── Nome do alimento + tipo
│   │       ├── Calorias
│   │       ├── IconButton Editar
│   │       ├── IconButton Deletar
│   │       └── AlertDialog de confirmação de exclusão
│   └── FloatingActionButton (Adicionar)
│
├── TelaFormulario
│   ├── TopAppBar (com botão Voltar)
│   ├── OutlinedTextField → Nome do Alimento
│   ├── OutlinedTextField → Calorias (teclado numérico)
│   ├── FlowRow de ChipTipoRefeicao (componente reutilizável)
│   │   └── 4x FilterChip (Café / Almoço / Jantar / Lanche)
│   ├── Card de erro (condicional)
│   ├── Button → Salvar / Salvar Alterações
│   └── OutlinedButton → Cancelar
│
└── TelaDetalhes
    ├── TopAppBar (com botão Voltar)
    ├── Card Resumo Geral
    │   ├── InfoItem Total
    │   ├── InfoItem Meta
    │   ├── InfoItem Saldo
    │   └── LinearProgressIndicator
    └── Lista de DetalheGrupo (por TipoRefeicao)
        ├── Cabeçalho com total do tipo
        └── Linha por alimento (nome + calorias)
```

## Componentes Reutilizáveis

| Componente | Arquivo | Usado em |
|---|---|---|
| `CardRefeicao` | Componentes.kt | TelaListagem |
| `ProgressoCalorias` | Componentes.kt | TelaListagem |
| `ChipTipoRefeicao` | Componentes.kt | TelaFormulario |
| `corDoTipo()` | Componentes.kt | TelaListagem, TelaDetalhes |

## Camadas da Arquitetura

```
UI Layer (Screens + Components)
        ↕ StateFlow / collectAsState
ViewModel Layer (RefeicaoViewModel)
        ↕ suspend functions / Flow
Data Layer
  ├── RefeicaoRepository
  ├── RefeicaoDao (Room)
  └── CaloriasDatabase (SQLite)
```
