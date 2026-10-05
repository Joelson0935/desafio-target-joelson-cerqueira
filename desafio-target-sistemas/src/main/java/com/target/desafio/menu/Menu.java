package com.target.desafio.menu;

import com.target.desafio.model.EstoqueWrapper;
import com.target.desafio.model.MovimentacaoEstoque;
import com.target.desafio.model.Produto;
import com.target.desafio.model.ResultadoJuros;
import com.target.desafio.model.ResumoComissao;
import com.target.desafio.model.TipoMovimentacao;
import com.target.desafio.model.VendasWrapper;
import com.target.desafio.service.ComissaoService;
import com.target.desafio.service.EstoqueInsuficienteException;
import com.target.desafio.service.EstoqueService;
import com.target.desafio.service.JurosService;
import com.target.desafio.util.JsonReader;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Menu {

    private static final Locale BR = Locale.forLanguageTag("pt-BR");
    private static final NumberFormat MOEDA = NumberFormat.getCurrencyInstance(BR);
    private static final DateTimeFormatter DATA_BR = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final Scanner scanner = new Scanner(System.in);

    private final JsonReader jsonReader = new JsonReader();
    private final ComissaoService comissaoService = new ComissaoService();
    private final JurosService jurosService = new JurosService();

    private EstoqueService estoqueServiceEmMemoria;

    public void iniciar() {
        boolean executando = true;
        while (executando) {
            exibirMenuPrincipal();
            String opcao = scanner.nextLine().trim();
            switch (opcao) {
                case "1" -> executarComissao();
                case "2" -> executarEstoque();
                case "3" -> executarJuros();
                case "0" -> {
                    executando = false;
                    System.out.println("\nEncerrando. Até logo!");
                }
                default -> System.out.println("\nOpção inválida. Tente novamente.");
            }
        }
    }

    private void exibirMenuPrincipal() {
        System.out.println();
        System.out.println("===== DESAFIO TARGET SISTEMAS =====");
        System.out.println("1 - Comissão de vendedores");
        System.out.println("2 - Movimentação de estoque");
        System.out.println("3 - Cálculo de juros por atraso");
        System.out.println("0 - Sair");
        System.out.print("Escolha uma opção: ");
    }

    private void executarComissao() {
        VendasWrapper wrapper = jsonReader.ler("vendas.json", VendasWrapper.class);
        List<ResumoComissao> resumos = comissaoService.calcularComissaoPorVendedor(wrapper.getVendas());

        System.out.println("\n--- Comissão por vendedor ---");
        System.out.printf("%-18s %8s %18s %15s%n", "Vendedor", "Vendas", "Total vendido", "Comissão");
        System.out.println("-".repeat(62));
        for (ResumoComissao r : resumos) {
            System.out.printf("%-18s %8d %18s %15s%n",
                    r.getVendedor(),
                    r.getQuantidadeVendas(),
                    MOEDA.format(r.getTotalVendido()),
                    MOEDA.format(r.getTotalComissao()));
        }
    }

    private void executarEstoque() {
        if (estoqueServiceEmMemoria == null) {
            EstoqueWrapper wrapper = jsonReader.ler("estoque.json", EstoqueWrapper.class);
            estoqueServiceEmMemoria = new EstoqueService(wrapper.getEstoque());
        }

        boolean noSubmenu = true;
        while (noSubmenu) {
            System.out.println("\n--- Estoque ---");
            System.out.println("1 - Listar produtos (com estoque atual)");
            System.out.println("2 - Registrar entrada");
            System.out.println("3 - Registrar saída");
            System.out.println("4 - Ver histórico de movimentações");
            System.out.println("0 - Voltar");
            System.out.print("Escolha uma opção: ");

            String opcao = scanner.nextLine().trim();
            switch (opcao) {
                case "1" -> listarProdutos();
                case "2" -> registrarMovimentacao(TipoMovimentacao.ENTRADA);
                case "3" -> registrarMovimentacao(TipoMovimentacao.SAIDA);
                case "4" -> listarMovimentacoes();
                case "0" -> noSubmenu = false;
                default -> System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private void listarProdutos() {
        System.out.println("\n--- Produtos em estoque ---");
        System.out.printf("%-8s %-28s %10s%n", "Código", "Descrição", "Estoque");
        System.out.println("-".repeat(48));
        for (Produto p : estoqueServiceEmMemoria.listarProdutos()) {
            System.out.printf("%-8d %-28s %10d%n",
                    p.getCodigoProduto(), p.getDescricaoProduto(), p.getEstoque());
        }
    }

    private void registrarMovimentacao(TipoMovimentacao tipo) {
        String rotulo = (tipo == TipoMovimentacao.ENTRADA) ? "ENTRADA" : "SAÍDA";
        System.out.println("\n--- Registrar " + rotulo + " ---");

        Integer codigo = lerInteiro("Código do produto: ");
        if (codigo == null) {
            return;
        }
        Produto produto = estoqueServiceEmMemoria.buscarProduto(codigo);
        if (produto == null) {
            System.out.println("Produto não encontrado para o código " + codigo + ".");
            return;
        }

        System.out.print("Descrição da movimentação: ");
        String descricao = scanner.nextLine().trim();

        Integer quantidade = lerInteiro("Quantidade: ");
        if (quantidade == null) {
            return;
        }

        try {
            MovimentacaoEstoque mov = estoqueServiceEmMemoria.registrarMovimentacao(codigo, tipo, descricao, quantidade);
            System.out.println("\nMovimentação #" + mov.getId() + " registrada com sucesso.");
            System.out.println("Produto: " + produto.getDescricaoProduto());
            System.out.println("Tipo: " + mov.getTipo());
            System.out.println("Quantidade: " + mov.getQuantidade());
            System.out.println("Estoque final do produto: " + mov.getEstoqueResultante());
        } catch (EstoqueInsuficienteException e) {
            System.out.println("\n" + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("\nNão foi possível registrar: " + e.getMessage());
        }
    }

    private void listarMovimentacoes() {
        List<MovimentacaoEstoque> movimentacoes = estoqueServiceEmMemoria.listarMovimentacoes();
        System.out.println("\n--- Histórico de movimentações ---");
        if (movimentacoes.isEmpty()) {
            System.out.println("Nenhuma movimentação registrada ainda.");
            return;
        }
        System.out.printf("%-5s %-9s %-8s %-24s %10s%n",
                "ID", "Tipo", "Produto", "Descrição", "Resultante");
        System.out.println("-".repeat(60));
        for (MovimentacaoEstoque m : movimentacoes) {
            System.out.printf("%-5d %-9s %-8d %-24s %10d%n",
                    m.getId(), m.getTipo(), m.getCodigoProduto(),
                    m.getDescricao(), m.getEstoqueResultante());
        }
    }

    private void executarJuros() {
        System.out.println("\n--- Cálculo de juros por atraso (2,5% ao dia) ---");

        BigDecimal valor = lerValorMonetario("Valor (ex: 1000,50): ");
        if (valor == null) {
            return;
        }

        LocalDate vencimento = lerData("Data de vencimento (dd/MM/aaaa): ");
        if (vencimento == null) {
            return;
        }

        try {
            ResultadoJuros r = jurosService.calcular(valor, vencimento);
            System.out.println("\nValor original: " + MOEDA.format(r.getValorOriginal()));
            System.out.println("Data de vencimento: " + r.getDataVencimento().format(DATA_BR));
            System.out.println("Data de hoje: " + LocalDate.now().format(DATA_BR));
            System.out.println("Dias de atraso: " + r.getDiasAtraso());
            System.out.println("Valor dos juros: " + MOEDA.format(r.getValorJuros()));
            System.out.println("Valor total atualizado: " + MOEDA.format(r.getValorTotal()));
        } catch (IllegalArgumentException e) {
            System.out.println("\nNão foi possível calcular: " + e.getMessage());
        }
    }

    private Integer lerInteiro(String rotulo) {
        System.out.print(rotulo);
        String entrada = scanner.nextLine().trim();
        try {
            return Integer.parseInt(entrada);
        } catch (NumberFormatException e) {
            System.out.println("Valor inválido. Informe um número inteiro.");
            return null;
        }
    }

    private BigDecimal lerValorMonetario(String rotulo) {
        System.out.print(rotulo);
        // Converte do padrão BR (1.234,56) para o formato aceito por BigDecimal (1234.56).
        String entrada = scanner.nextLine().trim().replace(".", "").replace(",", ".");
        try {
            return new BigDecimal(entrada);
        } catch (NumberFormatException e) {
            System.out.println("Valor inválido. Use o formato 1000,50.");
            return null;
        }
    }

    private LocalDate lerData(String rotulo) {
        System.out.print(rotulo);
        String entrada = scanner.nextLine().trim();
        try {
            return LocalDate.parse(entrada, DATA_BR);
        } catch (DateTimeParseException e) {
            System.out.println("Data inválida. Use o formato dd/MM/aaaa (ex: 31/12/2026).");
            return null;
        }
    }
}
