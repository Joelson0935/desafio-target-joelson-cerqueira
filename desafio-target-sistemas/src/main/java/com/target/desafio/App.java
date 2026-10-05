package com.target.desafio;

import com.target.desafio.menu.Menu;

/**
 * Ponto de entrada da aplicação.
 * Delega a interação ao menu de console, que reúne as três questões do desafio.
 */
public class App {

    public static void main(String[] args) {
        new Menu().iniciar();
    }
}
