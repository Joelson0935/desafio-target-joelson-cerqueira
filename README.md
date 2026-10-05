# Desafio Técnico - Target Sistemas (Desenvolvedor Jr.)

Aplicação Java de linha de comando que resolve as três questões propostas no desafio técnico da Target Sistemas. Cada questão é acessível por um menu interativo no console.

## Questões resolvidas

### 1. Comissão de vendedores
Lê um arquivo JSON com registros de vendas e calcula a comissão de cada vendedor, somando a comissão venda a venda conforme as faixas:

| Valor da venda | Comissão |
|----------------|----------|
| Abaixo de R$ 100,00 | Nenhuma (0%) |
| De R$ 100,00 a R$ 499,99 | 1% |
| A partir de R$ 500,00 | 5% |

Ao final, exibe por vendedor: quantidade de vendas, total vendido e total de comissão.

### 2. Movimentação de estoque
Permite lançar movimentações de **entrada** e **saída** sobre os produtos carregados de um JSON. Cada movimentação:

- recebe um **identificador único** (sequencial);
- possui uma **descrição** do tipo de movimentação;
- retorna a **quantidade final** do produto em estoque.

Uma saída que deixaria o estoque negativo é recusada, com aviso de estoque insuficiente, mantendo o estoque inalterado.

### 3. Cálculo de juros por atraso
A partir de um valor e de uma data de vencimento, calcula os juros até a data de hoje, considerando multa de **2,5% ao dia** (juros simples):

```
juros = valor x 0,025 x dias_de_atraso
```

Se a data de vencimento for hoje ou no futuro, não há atraso e os juros são zero.

## Tecnologias

- **Java 21** (LTS)
- **Maven** - build e gerenciamento de dependências
- **Jackson** (`jackson-databind`) - leitura/desserialização dos arquivos JSON
- **JUnit 5** - testes automatizados
- **BigDecimal** para todos os valores monetários, evitando os erros de arredondamento do tipo `double` em cálculos com dinheiro

## Estrutura do projeto

```
desafio-target-sistemas/
├── pom.xml
└── src/
    ├── main/
    │   ├── java/com/target/desafio/
    │   │   ├── App.java                      # ponto de entrada
    │   │   ├── menu/Menu.java                 # menu interativo de console
    │   │   ├── model/                         # Venda, Produto, Movimentacao, etc.
    │   │   ├── service/                       # regras de negocio das 3 questoes
    │   │   └── util/JsonReader.java           # leitura de JSON (Jackson)
    │   └── resources/
    │       ├── vendas.json                    # dados da Questao 1
    │       └── estoque.json                   # dados da Questao 2
    └── test/java/com/target/desafio/          # testes JUnit das 3 questoes
```

A organização segue uma separação por camadas: `model` (dados), `service` (regra de negócio), `util` (apoio) e `menu` (interação com o usuário).

## Pré-requisitos

- **JDK 21** ou superior
- **Maven 3.9+**

Verifique com:

```bash
java -version
mvn -version
```

## Como rodar

Todos os comandos são executados a partir da pasta do projeto Maven (`desafio-target-sistemas`).

```bash
cd desafio-target-sistemas
mvn compile exec:java
```

O menu interativo será exibido no console:

```
===== DESAFIO TARGET SISTEMAS =====
1 - Comissão de vendedores
2 - Movimentação de estoque
3 - Cálculo de juros por atraso
0 - Sair
Escolha uma opção:
```

> O projeto já está configurado para rodar com codificação **UTF-8** (via `exec-maven-plugin`), garantindo que os acentos sejam exibidos corretamente, inclusive no console do Windows.

### Entrada de dados (Questão 3)

- **Valor**: aceita o padrão brasileiro com vírgula como separador decimal (ex: `1000,50`).
- **Data de vencimento**: formato `dd/MM/aaaa` (ex: `31/12/2026`).

## Como rodar os testes

```bash
cd desafio-target-sistemas
mvn test
```

São 18 testes automatizados cobrindo:

- **Comissão**: cada faixa de valor, os limites de R$ 100 e R$ 500, e a consolidação por vendedor a partir do JSON.
- **Estoque**: carga do JSON, entrada, saída, bloqueio por estoque insuficiente, saída total, id único e validações.
- **Juros**: atraso de múltiplos dias, atraso de um dia com arredondamento, vencimento no futuro, vencimento hoje e valor inválido.

## Decisões de projeto

- **Java puro com Maven, sem Spring ou banco de dados.** As três questões são lógica de negócio pura; frameworks ou persistência adicionariam complexidade sem agregar ao que o desafio avalia. Os dados de exemplo são lidos dos arquivos JSON e processados em memória.
- **`BigDecimal` com arredondamento `HALF_UP`** (arredondamento comercial) para todos os cálculos monetários.
- **Identificador de movimentação em memória**, sequencial e reiniciado a cada execução, suficiente por não haver persistência.
```
