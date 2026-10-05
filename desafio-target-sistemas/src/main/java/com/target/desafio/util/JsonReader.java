package com.target.desafio.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

/**
 * Utilitário responsável por ler arquivos JSON localizados no classpath
 * (pasta src/main/resources) e convertê-los em objetos Java usando Jackson.
 * É reutilizado pelas questões de comissão e de estoque.
 */
public class JsonReader {

    private final ObjectMapper objectMapper;

    public JsonReader() {
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Lê um recurso do classpath e o desserializa para o tipo informado.
     *
     * @param nomeRecurso nome do arquivo no classpath, ex: "vendas.json"
     * @param tipo        classe alvo da desserialização
     * @return instância preenchida a partir do JSON
     */
    public <T> T ler(String nomeRecurso, Class<T> tipo) {
        try (InputStream entrada = getClass().getClassLoader().getResourceAsStream(nomeRecurso)) {
            if (entrada == null) {
                throw new IllegalArgumentException(
                        "Arquivo não encontrado no classpath: " + nomeRecurso);
            }
            return objectMapper.readValue(entrada, tipo);
        } catch (IOException e) {
            throw new UncheckedIOException("Falha ao ler o arquivo JSON: " + nomeRecurso, e);
        }
    }
}
