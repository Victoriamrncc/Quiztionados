package org.example;

public class Main {
    public static void main(String[] args) {
        Juego juego = new Juego();

        juego.cargarPreguntas("preguntas.json");

        Menu menu = new Menu(juego);

        menu.mostrarMenu();

        juego.guardarPuntaje("puntaje.ser");

        juego.cargarPuntaje("puntaje.ser");
    }
}