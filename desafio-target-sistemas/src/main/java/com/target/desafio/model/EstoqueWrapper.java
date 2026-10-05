package com.target.desafio.model;

import java.util.List;

/**
 * Wrapper que espelha a estrutura do arquivo estoque.json, cujo objeto raiz
 * possui a propriedade "estoque" contendo a lista de produtos.
 */
public class EstoqueWrapper {

    private List<Produto> estoque;

    public List<Produto> getEstoque() {
        return estoque;
    }

    public void setEstoque(List<Produto> estoque) {
        this.estoque = estoque;
    }
}
