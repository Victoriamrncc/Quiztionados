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

    @Override
    public String toString() {
        return "Puntaje actual: " + puntos;
    }
}
