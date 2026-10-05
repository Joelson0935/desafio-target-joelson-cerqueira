package com.target.desafio.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

public class JsonReader {

    private final ObjectMapper objectMapper;

    public JsonReader() {
        this.objectMapper = new ObjectMapper();
    }

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
