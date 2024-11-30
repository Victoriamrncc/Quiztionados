package org.example;

import java.io.*;

public class Puntaje implements Serializable {
    private int puntos;

    public int getPuntos() {
        return puntos;
    }

    public void sumarPuntos(int puntos) {
        this.puntos += puntos;
    }

    public void sumarPuntosPorTipo(String tipoPregunta, boolean esCorrecta) {
        if (!esCorrecta) return;

        int puntosAAsignar = switch (tipoPregunta.toLowerCase()) {
            case "verdadero/falso" -> 5;
            case "selección múltiple" -> 10;
            default -> 0;
        };

        this.puntos += puntosAAsignar;
    }


    public void guardarPuntaje(String archivo) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
            oos.writeObject(this);
            System.out.println("Puntaje guardado correctamente.\n");
        } catch (IOException e) {
            System.out.println("Error al guardar el puntaje: \n" + e.getMessage());
        }
    }

    public static Puntaje cargarPuntaje(String archivo) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            return (Puntaje) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al cargar el puntaje: \n" + e.getMessage());
            return null;
        }
    }

    @Override
    public String toString() {
        return "Puntaje actual: " + puntos;
    }
}
