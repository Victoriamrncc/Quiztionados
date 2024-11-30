package org.example;

public class Main {
    public static void main(String[] args) {
        Vidas juegoConVidas = new Vidas();

        juegoConVidas.cargarPreguntas("preguntas.json");

        Menu menu = new Menu(juegoConVidas);

        menu.mostrarMenu();

        juegoConVidas.guardarPuntaje("puntaje.ser");

        juegoConVidas.cargarPuntaje("puntaje.ser");
    }
}