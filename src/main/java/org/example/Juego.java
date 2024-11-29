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
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(nombreArchivo);

            if (inputStream == null) {
                throw new FileNotFoundException("Archivo no encontrado: " + nombreArchivo);
            }

            ObjectMapper mapper = new ObjectMapper();
            preguntas = mapper.readValue(inputStream, new TypeReference<List<Pregunta>>() {});
            preguntasFiltradas = preguntas;
        } catch (IOException e) {
            System.out.println("Error al cargar las preguntas: " + e.getMessage());
        }
    }

    public void seleccionarCategoria() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Selecciona la categoría del juego:");
        System.out.println("1. Historia");
        System.out.println("2. Ciencia");
        System.out.println("3. Geografía");
        System.out.print("Selecciona una opción: ");
        int categoriaSeleccionada = scanner.nextInt();
        scanner.nextLine();

        String categoria = "";
        switch (categoriaSeleccionada) {
            case 1:
                categoria = "Historia";
                break;
            case 2:
                categoria = "Ciencia";
                break;
            case 3:
                categoria = "Geografía";
                break;
            default:
                System.out.println("Opción no válida.\n");
                return;
        }

        String finalCategoria = categoria;
        preguntasFiltradas = preguntas.stream()
                .filter(pregunta -> pregunta.getCategoria().equalsIgnoreCase(finalCategoria))
                .collect(Collectors.toList());

        if (preguntasFiltradas.isEmpty()) {
            System.out.println("No hay preguntas disponibles para esta categoría.\n");
        } else {
            System.out.println("Categoría seleccionada: " + categoria);
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
        scanner.nextLine();

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
                System.out.println("Opción no válida.\n");
                return;
        }

        String finalDificultad = dificultad;
        preguntasFiltradas = preguntas.stream()
                .filter(pregunta -> pregunta.getDificultad().equalsIgnoreCase(finalDificultad))
                .collect(Collectors.toList());

        if (preguntasFiltradas.isEmpty()) {
            System.out.println("No hay preguntas disponibles para esta dificultad.\n");
        } else {
            System.out.println("Dificultad seleccionada: \n" + dificultad);
        }
    }

    public void jugar() {
        if (preguntasFiltradas == null || preguntasFiltradas.isEmpty()) {
            System.out.println("No hay preguntas cargadas.\n");
            return;
        }

        Scanner scanner = new Scanner(System.in);

        for (Pregunta pregunta : preguntasFiltradas) {
            System.out.println(pregunta.getEnunciado());
            if (pregunta.getTipoPregunta().equals("input")) {

                System.out.print("Escribe tu respuesta: ");
                String respuesta = scanner.nextLine();

                if (respuesta.equalsIgnoreCase(pregunta.getRespuestaCorrecta())) {
                    System.out.println("¡Correcto!\n");
                    puntaje.sumarPuntos(10);
                } else {
                    System.out.println("Incorrecto. La respuesta correcta era: " + pregunta.getRespuestaCorrecta() + "\n");
                }

            } else if (pregunta.getTipoPregunta().equals("selección múltiple")||pregunta.getTipoPregunta().equals("verdadero/falso")){

                for (int i = 0; i < pregunta.getOpciones().size(); i++) {
                    System.out.println((i + 1) + ". " + pregunta.getOpciones().get(i));
                }
                System.out.print("Tu respuesta (número): ");
                int respuesta = scanner.nextInt();
                String opcionSeleccionada = pregunta.getOpciones().get(respuesta - 1);

                if (opcionSeleccionada.equalsIgnoreCase(pregunta.getRespuestaCorrecta())) {
                    System.out.println("¡Correcto!\n");
                    puntaje.sumarPuntos(10);
                } else {
                    System.out.println("Incorrecto. La respuesta correcta era: " + pregunta.getRespuestaCorrecta() + "\n");
                }
                scanner.nextLine();
            }
        }
    }

    public void guardarPuntaje(String archivo) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
            oos.writeObject(puntaje);
            System.out.println("Puntaje guardado correctamente.\n");
        } catch (IOException e) {
            System.out.println("Error al guardar el puntaje: \n" + e.getMessage());
        }
    }

    public void cargarPuntaje(String archivo) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            this.puntaje = (Puntaje) ois.readObject();
            System.out.println("Puntaje cargado correctamente: \n" + puntaje);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al cargar el puntaje: \n" + e.getMessage());
        }
    }
}
