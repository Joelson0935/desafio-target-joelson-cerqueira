package com.target.desafio.model;

import java.math.BigDecimal;

/**
 * Resultado consolidado da comissão de um vendedor: quanto ele vendeu no total,
 * quantas vendas realizou e quanto gerou de comissão somando venda a venda.
 */
public class ResumoComissao {

    private final String vendedor;
    private final int quantidadeVendas;
    private final BigDecimal totalVendido;
    private final BigDecimal totalComissao;

    public ResumoComissao(String vendedor, int quantidadeVendas,
                          BigDecimal totalVendido, BigDecimal totalComissao) {
        this.vendedor = vendedor;
        this.quantidadeVendas = quantidadeVendas;
        this.totalVendido = totalVendido;
        this.totalComissao = totalComissao;
    }

    public String getVendedor() {
        return vendedor;
    }

    public int getQuantidadeVendas() {
        return quantidadeVendas;
    }

    public BigDecimal getTotalVendido() {
        return totalVendido;
    }

    public BigDecimal getTotalComissao() {
        return totalComissao;
    }
}
