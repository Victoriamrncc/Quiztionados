package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Juego {
    private List<Pregunta> preguntas;
    private Puntaje puntaje;
    private List<Pregunta> preguntasFiltradas;

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
            preguntasFiltradas = preguntas; // Al principio mostramos todas las preguntas
        } catch (IOException e) {
            System.out.println("Error al cargar las preguntas: " + e.getMessage());
        }
    }

    public void seleccionarDificultad() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Selecciona la dificultad del juego:");
        System.out.println("1. Fácil");
        System.out.println("2. Intermedio");
        System.out.println("3. Difícil");
        System.out.print("Selecciona una opción: ");
        int dificultadSeleccionada = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer

        String dificultad = "";
        switch (dificultadSeleccionada) {
            case 1:
                dificultad = "fácil";
                break;
            case 2:
                dificultad = "intermedio";
                break;
            case 3:
                dificultad = "difícil";
                break;
            default:
                System.out.println("Opción no válida.");
                return;
        }

        // Filtramos las preguntas según la dificultad seleccionada
        String finalDificultad = dificultad;
        preguntasFiltradas = preguntas.stream()
                .filter(pregunta -> {
                    // Usar la dificultad de la pregunta para hacer la comparación
                    return pregunta.getDificultad().equalsIgnoreCase(finalDificultad);
                })
                .collect(Collectors.toList());

        if (preguntasFiltradas.isEmpty()) {
            System.out.println("No hay preguntas disponibles para esta dificultad.");
        } else {
            System.out.println("Dificultad seleccionada: " + dificultad);
        }
    }


    public void jugar() {
        if (preguntasFiltradas == null || preguntasFiltradas.isEmpty()) {
            System.out.println("No hay preguntas cargadas.");
            return;
        }

        Scanner scanner = new Scanner(System.in);

        for (Pregunta pregunta : preguntasFiltradas) {
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

