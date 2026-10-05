package com.target.desafio.service;

import com.target.desafio.model.MovimentacaoEstoque;
import com.target.desafio.model.Produto;
import com.target.desafio.model.TipoMovimentacao;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class EstoqueService {

    private final Map<Integer, Produto> produtosPorCodigo = new LinkedHashMap<>();
    private final List<MovimentacaoEstoque> movimentacoes = new ArrayList<>();
    private final AtomicLong geradorDeIdSequencial = new AtomicLong(0);

    public EstoqueService(List<Produto> produtos) {
        if (produtos != null) {
            for (Produto produto : produtos) {
                produtosPorCodigo.put(produto.getCodigoProduto(), produto);
            }
        }
    }

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
        } else {
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
                geradorDeIdSequencial.incrementAndGet(),
                codigoProduto,
                tipo,
                descricao,
                quantidade,
                novoEstoque
        );
        movimentacoes.add(movimentacao);
        return movimentacao;
    }

    public Produto buscarProduto(int codigoProduto) {
        return produtosPorCodigo.get(codigoProduto);
    }

    public List<Produto> listarProdutos() {
        return new ArrayList<>(produtosPorCodigo.values());
    }

    public List<MovimentacaoEstoque> listarMovimentacoes() {
        return new ArrayList<>(movimentacoes);
    }
}
