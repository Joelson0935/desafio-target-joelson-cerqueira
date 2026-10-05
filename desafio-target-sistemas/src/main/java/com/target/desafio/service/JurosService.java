package com.target.desafio.service;

import com.target.desafio.model.ResultadoJuros;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Regra de negócio da Questão 3.
 *
 * A partir de um valor e de uma data de vencimento, calcula os juros até a
 * data de hoje, considerando multa de 2,5% ao dia (juros simples):
 *
 *     juros = valor * 0,025 * diasDeAtraso
 *
 * Se a data de vencimento for hoje ou no futuro, não há atraso e os juros
 * são zero.
 */
public class JurosService {

    /** Taxa de 2,5% ao dia. */
    private static final BigDecimal TAXA_DIARIA = new BigDecimal("0.025");

    /**
     * Calcula os juros usando a data de hoje como referência.
     */
    public ResultadoJuros calcular(BigDecimal valor, LocalDate dataVencimento) {
        return calcular(valor, dataVencimento, LocalDate.now());
    }

    /**
     * Calcula os juros usando uma data de referência explícita.
     * Útil para testes determinísticos (independentes do dia atual).
     *
     * @param valor          valor original (deve ser maior que zero)
     * @param dataVencimento data de vencimento
     * @param dataReferencia data considerada como "hoje"
     */
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

        long diasAtraso = ChronoUnit.DAYS.between(dataVencimento, dataReferencia);
        if (diasAtraso < 0) {
            // Vencimento no futuro: ainda não há atraso.
            diasAtraso = 0;
        }

        BigDecimal valorJuros = valor
                .multiply(TAXA_DIARIA)
                .multiply(BigDecimal.valueOf(diasAtraso))
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal valorOriginal = valor.setScale(2, RoundingMode.HALF_UP);
        BigDecimal valorTotal = valorOriginal.add(valorJuros).setScale(2, RoundingMode.HALF_UP);

        return new ResultadoJuros(valorOriginal, dataVencimento, diasAtraso, valorJuros, valorTotal);
    }
}
