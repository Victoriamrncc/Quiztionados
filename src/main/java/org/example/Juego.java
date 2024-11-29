package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.util.List;
import java.util.Scanner;

public class Juego {
    private List<Pregunta> preguntas;
    private Puntaje puntaje;

    public Juego() {
        this.puntaje = new Puntaje();
    }

    public void cargarPreguntas(String nombreArchivo) {
        try {
            // Usar ClassLoader para acceder al archivo en resources
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(nombreArchivo);

            if (inputStream == null) {
                throw new FileNotFoundException("Archivo no encontrado: " + nombreArchivo);
            }

            // Leer el archivo JSON
            ObjectMapper mapper = new ObjectMapper();
            preguntas = mapper.readValue(inputStream, new TypeReference<List<Pregunta>>() {});
        } catch (IOException e) {
            System.out.println("Error al cargar las preguntas: " + e.getMessage());
        }
    }

    public void jugar() {
        if (preguntas == null || preguntas.isEmpty()) {
            System.out.println("No hay preguntas cargadas.");
            return;
        }

        Scanner scanner = new Scanner(System.in);

        for (Pregunta pregunta : preguntas) {
            System.out.println(pregunta);
            System.out.print("Tu respuesta (número): ");
            int respuesta = scanner.nextInt();
            String opcionSeleccionada = pregunta.getOpciones().get(respuesta - 1);

            if (opcionSeleccionada.equalsIgnoreCase(pregunta.getRespuestaCorrecta())) {
                System.out.println("¡Correcto!");
                puntaje.sumarPuntos(10);
            } else {
                System.out.println("Incorrecto. La respuesta correcta era: " + pregunta.getRespuestaCorrecta());
            }
        }
    }

    public void guardarPuntaje(String archivo) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
            oos.writeObject(puntaje);
            System.out.println("Puntaje guardado correctamente.");
        } catch (IOException e) {
            System.out.println("Error al guardar el puntaje: " + e.getMessage());
        }
    }

    public void cargarPuntaje(String archivo) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            this.puntaje = (Puntaje) ois.readObject();
            System.out.println("Puntaje cargado correctamente: " + puntaje);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al cargar el puntaje: " + e.getMessage());
        }
    }
}
