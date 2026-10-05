package com.target.desafio.model;

import java.math.BigDecimal;

public class Venda {

    private String vendedor;
    private BigDecimal valor;

    public Venda() {
    }

    public Venda(String vendedor, BigDecimal valor) {
        this.vendedor = vendedor;
        this.valor = valor;
    }

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return "Venda{vendedor='" + vendedor + "', valor=" + valor + "}";
    }
}
