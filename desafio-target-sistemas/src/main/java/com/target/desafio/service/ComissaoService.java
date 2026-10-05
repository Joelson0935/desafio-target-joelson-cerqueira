package com.target.desafio.service;

import com.target.desafio.model.ResumoComissao;
import com.target.desafio.model.Venda;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Regra de negócio da Questão 1.
 *
 * Calcula a comissão de cada vendedor somando a comissão de cada venda,
 * segundo as faixas:
 *   - venda abaixo de R$ 100,00 ............ não gera comissão (0%)
 *   - venda de R$ 100,00 até R$ 499,99 ..... 1% de comissão
 *   - venda a partir de R$ 500,00 .......... 5% de comissão
 */
public class ComissaoService {

    private static final BigDecimal LIMITE_SEM_COMISSAO = new BigDecimal("100.00");
    private static final BigDecimal LIMITE_COMISSAO_MAIOR = new BigDecimal("500.00");

    private static final BigDecimal PERCENTUAL_1 = new BigDecimal("0.01"); // 1%
    private static final BigDecimal PERCENTUAL_5 = new BigDecimal("0.05"); // 5%

    /**
     * Calcula a comissão de uma única venda, já arredondada para 2 casas (HALF_UP).
     */
    public BigDecimal calcularComissaoVenda(BigDecimal valor) {
        if (valor == null || valor.compareTo(LIMITE_SEM_COMISSAO) < 0) {
            // Abaixo de R$ 100,00 (ou valor ausente): sem comissão.
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal percentual = (valor.compareTo(LIMITE_COMISSAO_MAIOR) >= 0)
                ? PERCENTUAL_5   // a partir de R$ 500,00
                : PERCENTUAL_1;  // de R$ 100,00 até R$ 499,99

        return valor.multiply(percentual).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Agrupa as vendas por vendedor e consolida total vendido e total de comissão.
     * A ordem de inserção dos vendedores é preservada (LinkedHashMap).
     */
    public List<ResumoComissao> calcularComissaoPorVendedor(List<Venda> vendas) {
        Map<String, BigDecimal> totalVendidoPorVendedor = new LinkedHashMap<>();
        Map<String, BigDecimal> totalComissaoPorVendedor = new LinkedHashMap<>();
        Map<String, Integer> quantidadePorVendedor = new LinkedHashMap<>();

        for (Venda venda : vendas) {
            String vendedor = venda.getVendedor();
            BigDecimal valor = venda.getValor();
            BigDecimal comissao = calcularComissaoVenda(valor);

            totalVendidoPorVendedor.merge(vendedor, valor, BigDecimal::add);
            totalComissaoPorVendedor.merge(vendedor, comissao, BigDecimal::add);
            quantidadePorVendedor.merge(vendedor, 1, Integer::sum);
        }

        List<ResumoComissao> resultado = new ArrayList<>();
        for (String vendedor : totalVendidoPorVendedor.keySet()) {
            resultado.add(new ResumoComissao(
                    vendedor,
                    quantidadePorVendedor.get(vendedor),
                    totalVendidoPorVendedor.get(vendedor).setScale(2, RoundingMode.HALF_UP),
                    totalComissaoPorVendedor.get(vendedor).setScale(2, RoundingMode.HALF_UP)
            ));
        }
        return resultado;
    }
}
