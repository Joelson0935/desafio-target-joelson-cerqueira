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
import static org.junit.jupiter.api.Assertions.assertTrue;

class EstoqueServiceTest {

    private EstoqueService service;

    @BeforeEach
    void recarregarEstoqueDoJsonParaIsolarCadaTeste() {
        EstoqueWrapper wrapper = new JsonReader().ler("estoque.json", EstoqueWrapper.class);
        service = new EstoqueService(wrapper.getEstoque());
    }

    @Test
    void carregaOsCincoProdutosDoJsonComDescricaoEEstoqueIniciais() {
        List<Produto> produtos = service.listarProdutos();
        assertEquals(5, produtos.size());
        assertEquals("Caneta Azul", service.buscarProduto(101).getDescricaoProduto());
        assertEquals(150, service.buscarProduto(101).getEstoque());
    }

    @Test
    void entradaDe50EmProdutoCom150AtualizaEstoqueResultantePara200() {
        MovimentacaoEstoque mov = service.registrarMovimentacao(
                101, TipoMovimentacao.ENTRADA, "Compra de fornecedor", 50);

        assertEquals(200, mov.getEstoqueResultante());
        assertEquals(200, service.buscarProduto(101).getEstoque());
    }

    @Test
    void saidaDe25EmProdutoCom75AtualizaEstoqueResultantePara50() {
        MovimentacaoEstoque mov = service.registrarMovimentacao(
                102, TipoMovimentacao.SAIDA, "Venda ao cliente", 25);

        assertEquals(50, mov.getEstoqueResultante());
        assertEquals(50, service.buscarProduto(102).getEstoque());
    }

    @Test
    void saidaMaiorQueOEstoqueEhRecusadaEMantemOEstoqueInalterado() {
        EstoqueInsuficienteException ex = assertThrows(EstoqueInsuficienteException.class, () ->
                service.registrarMovimentacao(105, TipoMovimentacao.SAIDA, "Saída inválida", 91));

        assertEquals(90, service.buscarProduto(105).getEstoque());
        assertTrue(ex.getMessage().contains("Estoque insuficiente"));
    }

    @Test
    void saidaIgualAoEstoqueTotalEhPermitidaEZeraOProduto() {
        MovimentacaoEstoque mov = service.registrarMovimentacao(
                105, TipoMovimentacao.SAIDA, "Saída total", 90);
        assertEquals(0, mov.getEstoqueResultante());
    }

    @Test
    void movimentacoesRecebemIdsUnicosEmSequenciaCrescente() {
        MovimentacaoEstoque m1 = service.registrarMovimentacao(101, TipoMovimentacao.ENTRADA, "Entrada 1", 10);
        MovimentacaoEstoque m2 = service.registrarMovimentacao(102, TipoMovimentacao.ENTRADA, "Entrada 2", 10);
        MovimentacaoEstoque m3 = service.registrarMovimentacao(103, TipoMovimentacao.SAIDA, "Saída 1", 10);

        assertEquals(1, m1.getId());
        assertEquals(2, m2.getId());
        assertEquals(3, m3.getId());
        assertEquals(3, service.listarMovimentacoes().size());
    }

    @Test
    void movimentacaoComCodigoDeProdutoInexistenteLancaIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                service.registrarMovimentacao(999, TipoMovimentacao.ENTRADA, "Produto inexistente", 5));
    }

    @Test
    void movimentacaoComQuantidadeZeroLancaIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () ->
                service.registrarMovimentacao(101, TipoMovimentacao.ENTRADA, "Qtd zero", 0));
    }
}
