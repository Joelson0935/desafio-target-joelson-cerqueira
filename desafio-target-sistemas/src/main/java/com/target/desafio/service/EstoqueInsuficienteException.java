package com.target.desafio.service;

// Lançada quando uma saída tentaria deixar o estoque negativo; a movimentação não é efetuada.
public class EstoqueInsuficienteException extends RuntimeException {

    public EstoqueInsuficienteException(String mensagem) {
        super(mensagem);
    }
}
