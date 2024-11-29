package org.example;
public class Main {
    public static void main(String[] args) {
        Juego juego = new Juego();

        juego.cargarPreguntas("preguntas.json");

        juego.jugar();

        juego.guardarPuntaje("puntaje.ser");

        juego.cargarPuntaje("puntaje.ser");
    }
}
