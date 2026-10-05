package com.target.desafio;

import com.target.desafio.model.ResumoComissao;
import com.target.desafio.model.VendasWrapper;
import com.target.desafio.service.ComissaoService;
import com.target.desafio.util.JsonReader;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ComissaoServiceTest {

    private final ComissaoService service = new ComissaoService();

    @Test
    void vendaDe90_75NaoGeraComissaoPorEstarAbaixoDe100() {
        assertEquals(new BigDecimal("0.00"), service.calcularComissaoVenda(new BigDecimal("90.75")));
    }

    @Test
    void vendaNoLimiteInferiorDe100Aplica1PorCentoResultando1_00() {
        assertEquals(new BigDecimal("1.00"), service.calcularComissaoVenda(new BigDecimal("100.00")));
    }

    @Test
    void vendaDe250_30DentroDaFaixaDe1PorCentoResulta2_50ComArredondamento() {
        assertEquals(new BigDecimal("2.50"), service.calcularComissaoVenda(new BigDecimal("250.30")));
    }

    @Test
    void vendaNoLimiteInferiorDe500Aplica5PorCentoResultando25_00() {
        assertEquals(new BigDecimal("25.00"), service.calcularComissaoVenda(new BigDecimal("500.00")));
    }

    @Test
    void consolidaTotaisDeComissaoPorVendedorAPartirDoVendasJson() {
        VendasWrapper wrapper = new JsonReader().ler("vendas.json", VendasWrapper.class);
        List<ResumoComissao> resumos = service.calcularComissaoPorVendedor(wrapper.getVendas());

        Map<String, ResumoComissao> porNome = resumos.stream()
                .collect(Collectors.toMap(ResumoComissao::getVendedor, Function.identity()));

        assertEquals(new BigDecimal("495.69"), porNome.get("João Silva").getTotalComissao());
        assertEquals(new BigDecimal("465.96"), porNome.get("Maria Souza").getTotalComissao());
        assertEquals(new BigDecimal("379.38"), porNome.get("Carlos Oliveira").getTotalComissao());
        assertEquals(new BigDecimal("404.99"), porNome.get("Ana Lima").getTotalComissao());

        assertEquals(new BigDecimal("10754.70"), porNome.get("João Silva").getTotalVendido());
        assertEquals(10, porNome.get("João Silva").getQuantidadeVendas());
    }
}
