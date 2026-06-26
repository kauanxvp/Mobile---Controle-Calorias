# 🥗 Contador de Calorias com Refeições

Aplicativo Android desenvolvido em **Kotlin + Jetpack Compose** com persistência local via **Room**.

## 📱 Funcionalidades

- ✅ **Adicionar** refeições com nome, calorias e tipo (Café, Almoço, Jantar, Lanche)
- ✅ **Listar** todas as refeições do dia com agrupamento por tipo
- ✅ **Editar** qualquer refeição existente
- ✅ **Excluir** refeição com confirmação via diálogo
- ✅ **Progresso visual** circular de calorias consumidas vs. meta diária (2000 kcal)
- ✅ **Resumo por tipo** de refeição com totais individuais
- ✅ **Persistência local** — dados sobrevivem ao fechamento do app (Room/SQLite)
- ✅ **Validação de formulário** — campos obrigatórios e verificação de valor numérico

## 🏗️ Arquitetura

```
MVVM (Model-View-ViewModel)
├── data/
│   ├── model/     → Refeicao.kt, TipoRefeicao.kt
│   ├── dao/       → RefeicaoDao.kt
│   └── database/  → CaloriasDatabase.kt, RefeicaoRepository.kt
├── ui/
│   ├── screens/   → TelaListagem.kt, TelaFormulario.kt, TelaDetalhes.kt
│   ├── components/→ Componentes.kt (CardRefeicao, ProgressoCalorias, ChipTipoRefeicao)
│   └── theme/     → Theme.kt
└── viewmodel/     → RefeicaoViewModel.kt
```

## 🚀 Como rodar no Android Studio

1. Abra o projeto no Android Studio Hedgehog ou superior.
2. Aguarde a sincronização do Gradle.
3. Conecte um dispositivo ou inicie um emulador Android (API 26+).
4. Clique em **Run ▶**.

## 📋 Sequência de Commits sugerida

```bash
git init
git add .
git commit -m "feat: estrutura inicial do projeto Android"

git commit -m "feat: cria entidade Refeicao e enum TipoRefeicao"

git commit -m "feat: implementa DAO e banco Room (CaloriasDatabase)"

git commit -m "feat: cria RefeicaoRepository e RefeicaoViewModel"

git commit -m "feat: implementa TelaListagem com lista e progresso"

git commit -m "feat: implementa TelaFormulario com validação"

git commit -m "feat: implementa TelaDetalhes com resumo por tipo"

git commit -m "feat: adiciona componentes reutilizáveis (CardRefeicao, ProgressoCalorias)"

git commit -m "docs: adiciona DECISOES.md, MAPEAMENTO.md e ESTADO.md"

git commit -m "fix: ajusta validação de calorias e mensagens de erro"
```

## 📦 Dependências principais

| Biblioteca | Versão | Uso |
|---|---|---|
| Jetpack Compose BOM | 2024.09.00 | UI declarativa |
| Room | 2.6.1 | Persistência local |
| Navigation Compose | 2.8.5 | Navegação entre telas |
| ViewModel Compose | 2.8.7 | Gerenciamento de estado |
| Material3 | via BOM | Design system |

## 📄 Documentação

- [`docs/DECISOES.md`](docs/DECISOES.md) — Justificativas técnicas
- [`docs/MAPEAMENTO.md`](docs/MAPEAMENTO.md) — Hierarquia de componentes
- [`docs/ESTADO.md`](docs/ESTADO.md) — Plano de estado e persistência
