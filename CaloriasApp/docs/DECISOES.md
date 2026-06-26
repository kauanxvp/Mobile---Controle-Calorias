# Decisões de Projeto – Contador de Calorias

## Tecnologia escolhida

- [x] Nativo (Kotlin + Jetpack Compose)

**Por quê?**  
O professor disponibilizou um comparativo técnico entre React Native (Expo) e Jetpack Compose. Optei pelo desenvolvimento nativo com Kotlin porque:
1. O Android Studio já estava configurado para uso com emulador na apresentação.
2. O Jetpack Compose oferece uma abordagem declarativa moderna, com melhor performance no Android por não depender de bridge JavaScript.
3. O acesso ao Room (banco de dados nativo do Android) é direto, sem necessidade de bibliotecas de terceiros para persistência.
4. A documentação oficial do Android é mais completa para Kotlin nativo.

---

## Persistência escolhida

**Biblioteca:** Room (androidx.room)  
**Por quê?**  
O Room é a solução oficial do Android para persistência local com SQLite. Ele oferece:
- Verificação de queries em tempo de compilação (evita erros em runtime).
- Integração nativa com Kotlin Coroutines e Flow para atualizações reativas da UI.
- Migrations seguras entre versões.
- Zero boilerplate comparado ao SQLite puro.

Os dados sobrevivem ao fechamento do app pois são gravados em banco SQLite no armazenamento interno do dispositivo.

---

## Estrutura de navegação

**Quantidade de telas:** 3

| Tela | Rota | Função |
|------|------|--------|
| TelaListagem | `listagem` | Tela inicial — exibe o progresso de calorias do dia e a lista de refeições agrupadas por tipo |
| TelaFormulario | `formulario` | Cadastro e edição de refeições — campos nome, calorias e tipo |
| TelaDetalhes | `detalhes` | Visualização detalhada com resumo por tipo e barra de progresso linear |

**Como se comunicam:**  
Todas as telas compartilham o mesmo `RefeicaoViewModel`. A navegação usa `NavHostController` do Jetpack Navigation Compose. Dados entre telas são passados via ViewModel (sem Bundle/parcelable), o que simplifica o fluxo.

---

## Funcionalidade que eu queria implementar mas não deu tempo

**O quê:** Histórico de dias anteriores com calendário interativo.  
**Como começaria:**  
Adicionaria um `DatePickerDialog` na `TelaListagem` para o usuário selecionar qualquer data. O `RefeicaoViewModel` já possui o StateFlow `_dataSelecionada` que troca as refeições exibidas ao mudar de data — a base já está pronta, faltaria apenas a UI do calendário.

---

## Trecho que eu escrevi sem ajuda de IA

```kotlin
// Em RefeicaoViewModel.kt — função salvar()
fun salvar(): Boolean {
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
    // ...restante do bloco de persistência
}
```

**O que faz:** Valida os campos do formulário antes de salvar. Retorna `false` e exibe mensagem de erro se o nome estiver em branco ou se o valor de calorias não for um número positivo válido. Retorna `true` apenas quando os dados são válidos, permitindo que a tela feche automaticamente após o salvamento.

---

## Partes feitas com auxílio de IA

- Estrutura inicial dos arquivos Kotlin (boilerplate Room, ViewModel, Compose).
- Sugestão de nomes de componentes e organização de pastas.

## Partes feitas manualmente (com ajuste substancial)

- Lógica de validação do formulário (`// AJUSTADO MANUALMENTE` no código).
- Paleta de cores e identidade visual do app.
- Agrupamento de refeições por tipo na listagem.
- Cálculo de saldo calórico (meta - consumido).
