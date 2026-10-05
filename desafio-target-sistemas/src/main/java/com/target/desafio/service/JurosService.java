package com.target.desafio.service;

import com.target.desafio.model.ResultadoJuros;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class JurosService {

    private static final BigDecimal TAXA_DIARIA_DE_ATRASO = new BigDecimal("0.025");

    public ResultadoJuros calcular(BigDecimal valor, LocalDate dataVencimento) {
        return calcular(valor, dataVencimento, LocalDate.now());
    }

    public ResultadoJuros calcular(BigDecimal valor, LocalDate dataVencimento, LocalDate dataReferencia) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor deve ser maior que zero.");
        }
        if (dataVencimento == null) {
            throw new IllegalArgumentException("A data de vencimento é obrigatória.");
        }
        if (dataReferencia == null) {
            throw new IllegalArgumentException("A data de referência é obrigatória.");
        }

        long diasAtraso = calcularDiasDeAtraso(dataVencimento, dataReferencia);

        BigDecimal valorJuros = valor
                .multiply(TAXA_DIARIA_DE_ATRASO)
                .multiply(BigDecimal.valueOf(diasAtraso))
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal valorOriginal = valor.setScale(2, RoundingMode.HALF_UP);
        BigDecimal valorTotal = valorOriginal.add(valorJuros).setScale(2, RoundingMode.HALF_UP);

        return new ResultadoJuros(valorOriginal, dataVencimento, diasAtraso, valorJuros, valorTotal);
    }

    private long calcularDiasDeAtraso(LocalDate dataVencimento, LocalDate dataReferencia) {
        long dias = ChronoUnit.DAYS.between(dataVencimento, dataReferencia);
        return Math.max(dias, 0);
    }
}
