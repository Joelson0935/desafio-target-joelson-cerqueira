package com.target.desafio.service;

import com.target.desafio.model.MovimentacaoEstoque;
import com.target.desafio.model.Produto;
import com.target.desafio.model.TipoMovimentacao;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Regra de negócio da Questão 2.
 *
 * Mantém os produtos em memória e permite lançar movimentações de entrada e
 * saída. Cada movimentação recebe um identificador único e, ao final, informa
 * a quantidade resultante do produto em estoque.
 *
 * Regras:
 *   - ENTRADA soma a quantidade ao estoque do produto.
 *   - SAIDA subtrai a quantidade; se o estoque for insuficiente, a movimentação
 *     é recusada (EstoqueInsuficienteException) e o estoque não fica negativo.
 */
public class EstoqueService {

    /** Produtos indexados pelo código, preservando a ordem de carga. */
    private final Map<Integer, Produto> produtosPorCodigo = new LinkedHashMap<>();

    /** Histórico das movimentações efetuadas, na ordem em que ocorreram. */
    private final List<MovimentacaoEstoque> movimentacoes = new ArrayList<>();

    /** Gerador do identificador único de movimentação (1, 2, 3, ...). */
    private final AtomicLong sequenciaId = new AtomicLong(0);

    public EstoqueService(List<Produto> produtos) {
        if (produtos != null) {
            for (Produto produto : produtos) {
                produtosPorCodigo.put(produto.getCodigoProduto(), produto);
            }
        }
    }

    /**
     * Registra uma movimentação de estoque para o produto informado.
     *
     * @param codigoProduto código do produto a movimentar
     * @param tipo          ENTRADA ou SAIDA
     * @param descricao     descrição do tipo de movimentação realizada
     * @param quantidade    quantidade movimentada (deve ser maior que zero)
     * @return a movimentação criada, contendo o id único e o estoque resultante
     * @throws IllegalArgumentException        se o produto não existir ou a quantidade for inválida
     * @throws EstoqueInsuficienteException    se a saída deixaria o estoque negativo
     */
    public MovimentacaoEstoque registrarMovimentacao(int codigoProduto, TipoMovimentacao tipo,
                                                     String descricao, int quantidade) {
        Produto produto = produtosPorCodigo.get(codigoProduto);
        if (produto == null) {
            throw new IllegalArgumentException("Produto não encontrado: código " + codigoProduto);
        }
        if (tipo == null) {
            throw new IllegalArgumentException("Tipo de movimentação é obrigatório.");
        }
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }

        int estoqueAtual = produto.getEstoque();
        int novoEstoque;

        if (tipo == TipoMovimentacao.ENTRADA) {
            novoEstoque = estoqueAtual + quantidade;
        } else { // SAIDA
            if (quantidade > estoqueAtual) {
                throw new EstoqueInsuficienteException(
                        "Estoque insuficiente para o produto " + codigoProduto
                                + " (" + produto.getDescricaoProduto() + "). "
                                + "Disponível: " + estoqueAtual + ", solicitado: " + quantidade + ".");
            }
            novoEstoque = estoqueAtual - quantidade;
        }

        produto.setEstoque(novoEstoque);

        MovimentacaoEstoque movimentacao = new MovimentacaoEstoque(
                sequenciaId.incrementAndGet(),
                codigoProduto,
                tipo,
                descricao,
                quantidade,
                novoEstoque
        );
        movimentacoes.add(movimentacao);
        return movimentacao;
    }

    /** Retorna o produto pelo código, ou null se não existir. */
    public Produto buscarProduto(int codigoProduto) {
        return produtosPorCodigo.get(codigoProduto);
    }

    /** Lista os produtos com seu estoque atual, na ordem de carga. */
    public List<Produto> listarProdutos() {
        return new ArrayList<>(produtosPorCodigo.values());
    }

    /** Lista o histórico de movimentações já efetuadas. */
    public List<MovimentacaoEstoque> listarMovimentacoes() {
        return new ArrayList<>(movimentacoes);
    }
}
