package com.target.desafio;

import com.target.desafio.model.ResumoComissao;
import com.target.desafio.model.Venda;
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
    void vendaAbaixoDe100NaoGeraComissao() {
        assertEquals(new BigDecimal("0.00"), service.calcularComissaoVenda(new BigDecimal("90.75")));
    }

    @Test
    void vendaEntre100e499Gera1PorCento() {
        // 250.30 * 1% = 2.503 -> 2.50
        assertEquals(new BigDecimal("2.50"), service.calcularComissaoVenda(new BigDecimal("250.30")));
    }

    @Test
    void vendaExatamente500Gera5PorCento() {
        // 500.00 * 5% = 25.00 (limite inferior da faixa de 5%)
        assertEquals(new BigDecimal("25.00"), service.calcularComissaoVenda(new BigDecimal("500.00")));
    }

    @Test
    void vendaExatamente100Gera1PorCento() {
        // 100.00 * 1% = 1.00 (limite inferior da faixa de 1%)
        assertEquals(new BigDecimal("1.00"), service.calcularComissaoVenda(new BigDecimal("100.00")));
    }

    @Test
    void consolidaComissaoPorVendedorConformeArquivoJson() {
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
