package com.target.desafio.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ResultadoJuros {

    private final BigDecimal valorOriginal;
    private final LocalDate dataVencimento;
    private final long diasAtraso;
    private final BigDecimal valorJuros;
    private final BigDecimal valorTotal;

    public ResultadoJuros(BigDecimal valorOriginal, LocalDate dataVencimento,
                          long diasAtraso, BigDecimal valorJuros, BigDecimal valorTotal) {
        this.valorOriginal = valorOriginal;
        this.dataVencimento = dataVencimento;
        this.diasAtraso = diasAtraso;
        this.valorJuros = valorJuros;
        this.valorTotal = valorTotal;
    }

    public BigDecimal getValorOriginal() {
        return valorOriginal;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public long getDiasAtraso() {
        return diasAtraso;
    }

    public BigDecimal getValorJuros() {
        return valorJuros;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }
}
