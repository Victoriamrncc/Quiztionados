package org.example;

import java.io.*;

public class Puntaje implements Serializable {
    private int puntos;

    public int getPuntos() {
        return puntos;
    }

    public void sumarPuntosPorTipo(String tipoPregunta, boolean esCorrecta) {
        if (!esCorrecta) return;

        int puntosAAsignar = switch (tipoPregunta.toLowerCase()) {
            case "verdadero/falso" -> 5;
            case "selección múltiple" -> 10;
            case "input" -> 15;
            default -> 0;
        };

        this.puntos += puntosAAsignar;
    }

    @Override
    public String toString() {
        return "Puntaje actual: " + puntos;
    }
}
