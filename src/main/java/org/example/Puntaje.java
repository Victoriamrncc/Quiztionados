package org.example;

import java.io.Serializable;

public class Puntaje implements Serializable {
    private int puntos;

    public int getPuntos() {
        return puntos;
    }

    public void sumarPuntos(int puntos) {
        this.puntos += puntos;
    }

    public void sumarPuntosPorTipo(String tipoPregunta, boolean esCorrecta) {
        int puntosAAsignar = 0;

        if (tipoPregunta.equalsIgnoreCase("verdadero/falso")) {
            puntosAAsignar = esCorrecta ? 5 : 0;
        } else if (tipoPregunta.equalsIgnoreCase("input")) {
            puntosAAsignar = esCorrecta ? 15 : 0;
        } else if (tipoPregunta.equalsIgnoreCase("selección múltiple")) {
            puntosAAsignar = esCorrecta ? 10 : 0;
        }

        if (esCorrecta) {
            this.puntos += puntosAAsignar;
        }
    }

    @Override
    public String toString() {
        return "Puntaje actual: " + puntos;
    }
}
