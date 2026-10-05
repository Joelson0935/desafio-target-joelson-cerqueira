package com.target.desafio.model;

/**
 * Representa uma movimentação de estoque já efetuada.
 * Cada movimentação possui um identificador único, o produto afetado,
 * o tipo (entrada/saída), uma descrição, a quantidade movimentada e
 * a quantidade final do produto em estoque após o lançamento.
 */
public class MovimentacaoEstoque {

    private final long id;
    private final int codigoProduto;
    private final TipoMovimentacao tipo;
    private final String descricao;
    private final int quantidade;
    private final int estoqueResultante;

    public MovimentacaoEstoque(long id, int codigoProduto, TipoMovimentacao tipo,
                               String descricao, int quantidade, int estoqueResultante) {
        this.id = id;
        this.codigoProduto = codigoProduto;
        this.tipo = tipo;
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.estoqueResultante = estoqueResultante;
    }

    public long getId() {
        return id;
    }

    public int getCodigoProduto() {
        return codigoProduto;
    }

    public TipoMovimentacao getTipo() {
        return tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public int getEstoqueResultante() {
        return estoqueResultante;
    }

    @Override
    public String toString() {
        return "Movimentacao #" + id
                + " [" + tipo + "] produto=" + codigoProduto
                + ", qtd=" + quantidade
                + ", descricao='" + descricao + '\''
                + ", estoqueResultante=" + estoqueResultante;
    }
}
