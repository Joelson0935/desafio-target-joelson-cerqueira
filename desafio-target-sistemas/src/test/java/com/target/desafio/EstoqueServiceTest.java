package com.target.desafio;

import com.target.desafio.model.EstoqueWrapper;
import com.target.desafio.model.MovimentacaoEstoque;
import com.target.desafio.model.Produto;
import com.target.desafio.model.TipoMovimentacao;
import com.target.desafio.service.EstoqueInsuficienteException;
import com.target.desafio.service.EstoqueService;
import com.target.desafio.util.JsonReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EstoqueServiceTest {

    private EstoqueService service;

    @BeforeEach
    void setUp() {
        // Recarrega os produtos do JSON a cada teste, garantindo isolamento.
        EstoqueWrapper wrapper = new JsonReader().ler("estoque.json", EstoqueWrapper.class);
        service = new EstoqueService(wrapper.getEstoque());
    }

    @Test
    void carregaProdutosDoJson() {
        List<Produto> produtos = service.listarProdutos();
        assertEquals(5, produtos.size());
        assertEquals("Caneta Azul", service.buscarProduto(101).getDescricaoProduto());
        assertEquals(150, service.buscarProduto(101).getEstoque());
    }

    @Test
    void entradaSomaAoEstoqueERetornaEstoqueResultante() {
        MovimentacaoEstoque mov = service.registrarMovimentacao(
                101, TipoMovimentacao.ENTRADA, "Compra de fornecedor", 50);

        assertEquals(200, mov.getEstoqueResultante());   // 150 + 50
        assertEquals(200, service.buscarProduto(101).getEstoque());
    }

    @Test
    void saidaSubtraiDoEstoqueERetornaEstoqueResultante() {
        MovimentacaoEstoque mov = service.registrarMovimentacao(
                102, TipoMovimentacao.SAIDA, "Venda ao cliente", 25);

        assertEquals(50, mov.getEstoqueResultante());    // 75 - 25
        assertEquals(50, service.buscarProduto(102).getEstoque());
    }

    @Test
    void saidaAcimaDoEstoqueEhRecusadaEnaoAlteraOEstoque() {
        EstoqueInsuficienteException ex = assertThrows(EstoqueInsuficienteException.class, () ->
                service.registrarMovimentacao(105, TipoMovimentacao.SAIDA, "Saída inválida", 91));

        // Estoque permanece intacto (90) e nunca fica negativo.
        assertEquals(90, service.buscarProduto(105).getEstoque());
        // A mensagem informa o problema ao usuário.
        org.junit.jupiter.api.Assertions.assertTrue(ex.getMessage().contains("Estoque insuficiente"));
    }

    @Test
    void saidaDeTodoOEstoqueEhPermitidaEZeraOProduto() {
        MovimentacaoEstoque mov = service.registrarMovimentacao(
                105, TipoMovimentacao.SAIDA, "Saída total", 90);
        assertEquals(0, mov.getEstoqueResultante());     // 90 - 90, nunca negativo
    }

    @Test
    void cadaMovimentacaoRecebeIdUnicoIncremental() {
        MovimentacaoEstoque m1 = service.registrarMovimentacao(101, TipoMovimentacao.ENTRADA, "Entrada 1", 10);
        MovimentacaoEstoque m2 = service.registrarMovimentacao(102, TipoMovimentacao.ENTRADA, "Entrada 2", 10);
        MovimentacaoEstoque m3 = service.registrarMovimentacao(103, TipoMovimentacao.SAIDA, "Saída 1", 10);

        assertEquals(1, m1.getId());
        assertEquals(2, m2.getId());
        assertEquals(3, m3.getId());
        assertEquals(3, service.listarMovimentacoes().size());
    }

    @Test
    void produtoInexistenteLancaExcecao() {
        assertThrows(IllegalArgumentException.class, () ->
                service.registrarMovimentacao(999, TipoMovimentacao.ENTRADA, "Produto inexistente", 5));
    }

    @Test
    void quantidadeInvalidaLancaExcecao() {
        assertThrows(IllegalArgumentException.class, () ->
                service.registrarMovimentacao(101, TipoMovimentacao.ENTRADA, "Qtd zero", 0));
    }
}
