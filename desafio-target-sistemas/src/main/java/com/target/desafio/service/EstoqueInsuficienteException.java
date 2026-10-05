package com.target.desafio.service;

/**
 * Lançada quando uma movimentação de saída tentaria deixar o estoque
 * do produto negativo. A movimentação não é efetuada nesse caso.
 */
public class EstoqueInsuficienteException extends RuntimeException {

    public EstoqueInsuficienteException(String mensagem) {
        super(mensagem);
    }
}
