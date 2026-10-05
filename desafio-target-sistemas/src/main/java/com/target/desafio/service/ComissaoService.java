package com.target.desafio.service;

import com.target.desafio.model.ResumoComissao;
import com.target.desafio.model.Venda;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ComissaoService {

    private static final BigDecimal VALOR_MINIMO_PARA_COMISSAO = new BigDecimal("100.00");
    private static final BigDecimal VALOR_MINIMO_COMISSAO_MAIOR = new BigDecimal("500.00");

    private static final BigDecimal TAXA_UM_PORCENTO = new BigDecimal("0.01");
    private static final BigDecimal TAXA_CINCO_PORCENTO = new BigDecimal("0.05");

    public BigDecimal calcularComissaoVenda(BigDecimal valor) {
        if (valor == null || valor.compareTo(VALOR_MINIMO_PARA_COMISSAO) < 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal taxaAplicavel = valor.compareTo(VALOR_MINIMO_COMISSAO_MAIOR) >= 0
                ? TAXA_CINCO_PORCENTO
                : TAXA_UM_PORCENTO;

        return valor.multiply(taxaAplicavel).setScale(2, RoundingMode.HALF_UP);
    }

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
