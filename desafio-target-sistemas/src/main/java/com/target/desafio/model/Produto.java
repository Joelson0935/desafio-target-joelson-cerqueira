package com.target.desafio.model;

/**
 * Representa um produto do depósito e sua quantidade atual em estoque.
 * O campo estoque é alterado conforme as movimentações de entrada e saída.
 */
public class Produto {

    private int codigoProduto;
    private String descricaoProduto;
    private int estoque;

    public Produto() {
    }

    public Produto(int codigoProduto, String descricaoProduto, int estoque) {
        this.codigoProduto = codigoProduto;
        this.descricaoProduto = descricaoProduto;
        this.estoque = estoque;
    }

    public int getCodigoProduto() {
        return codigoProduto;
    }

    public void setCodigoProduto(int codigoProduto) {
        this.codigoProduto = codigoProduto;
    }

    public String getDescricaoProduto() {
        return descricaoProduto;
    }

    public void setDescricaoProduto(String descricaoProduto) {
        this.descricaoProduto = descricaoProduto;
    }

    public int getEstoque() {
        return estoque;
    }

    public void setEstoque(int estoque) {
        this.estoque = estoque;
    }

    @Override
    public String toString() {
        return "Produto{codigo=" + codigoProduto
                + ", descricao='" + descricaoProduto + '\''
                + ", estoque=" + estoque + '}';
    }
}
