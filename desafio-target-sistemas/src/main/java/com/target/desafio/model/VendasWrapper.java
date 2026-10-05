package com.target.desafio.model;

import java.util.List;

/**
 * Wrapper que espelha a estrutura do arquivo vendas.json, cujo objeto raiz
 * possui a propriedade "vendas" contendo a lista de registros de venda.
 */
public class VendasWrapper {

    private List<Venda> vendas;

    public List<Venda> getVendas() {
        return vendas;
    }

    public void setVendas(List<Venda> vendas) {
        this.vendas = vendas;
    }
}
