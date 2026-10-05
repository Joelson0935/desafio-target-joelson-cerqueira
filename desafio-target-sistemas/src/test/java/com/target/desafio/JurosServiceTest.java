package com.target.desafio;

import com.target.desafio.model.ResultadoJuros;
import com.target.desafio.service.JurosService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JurosServiceTest {

    private final JurosService service = new JurosService();

    @Test
    void dezDiasDeAtrasoSobreMilReaisGeram250DeJurosE1250DeTotal() {
        LocalDate vencimento = LocalDate.of(2026, 1, 1);
        LocalDate hoje = LocalDate.of(2026, 1, 11);

        ResultadoJuros r = service.calcular(new BigDecimal("1000.00"), vencimento, hoje);

        assertEquals(10, r.getDiasAtraso());
        assertEquals(new BigDecimal("250.00"), r.getValorJuros());
        assertEquals(new BigDecimal("1250.00"), r.getValorTotal());
    }

    @Test
    void umDiaDeAtrasoSobre250_30GeraJurosArredondadoPara6_26() {
        LocalDate vencimento = LocalDate.of(2026, 3, 10);
        LocalDate hoje = LocalDate.of(2026, 3, 11);

        ResultadoJuros r = service.calcular(new BigDecimal("250.30"), vencimento, hoje);

        assertEquals(1, r.getDiasAtraso());
        assertEquals(new BigDecimal("6.26"), r.getValorJuros());
        assertEquals(new BigDecimal("256.56"), r.getValorTotal());
    }

    @Test
    void vencimentoNoFuturoResultaEmZeroDiasDeAtrasoESemJuros() {
        LocalDate vencimento = LocalDate.of(2026, 12, 31);
        LocalDate hoje = LocalDate.of(2026, 1, 11);

        ResultadoJuros r = service.calcular(new BigDecimal("1000.00"), vencimento, hoje);

        assertEquals(0, r.getDiasAtraso());
        assertEquals(new BigDecimal("0.00"), r.getValorJuros());
        assertEquals(new BigDecimal("1000.00"), r.getValorTotal());
    }

    @Test
    void vencimentoNaPropriaDataDeHojeNaoGeraJuros() {
        LocalDate hoje = LocalDate.of(2026, 1, 11);

        ResultadoJuros r = service.calcular(new BigDecimal("500.00"), hoje, hoje);

        assertEquals(0, r.getDiasAtraso());
        assertEquals(new BigDecimal("0.00"), r.getValorJuros());
        assertEquals(new BigDecimal("500.00"), r.getValorTotal());
    }

    @Test
    void valorZeroOuNegativoLancaIllegalArgumentException() {
        LocalDate hoje = LocalDate.of(2026, 1, 11);
        assertThrows(IllegalArgumentException.class, () ->
                service.calcular(new BigDecimal("0.00"), LocalDate.of(2026, 1, 1), hoje));
    }
}
