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
        String categoria = null;

        while (categoria == null) {
            System.out.println("Selecciona la categoría del juego:");
            System.out.println("1. Historia");
            System.out.println("2. Ciencia");
            System.out.println("3. Geografía");
            System.out.print("Selecciona una opción: ");
            int categoriaSeleccionada = scanner.nextInt();
            scanner.nextLine();

            categoria = switch (categoriaSeleccionada) {
                case 1 -> "Historia";
                case 2 -> "Ciencia";
                case 3 -> "Geografía";
                default -> {
                    System.out.println("Opción no válida. Por favor, selecciona una opción válida.\n");
                    yield null;
                }
            };
        }

        System.out.println("Categoría seleccionada: " + categoria);

        String finalCategoria = categoria;
        preguntasFiltradas = preguntas.stream()
                .filter(pregunta -> pregunta.getCategoria() != null &&
                        pregunta.getCategoria().trim().equalsIgnoreCase(finalCategoria))
                .collect(Collectors.toList());

        if (preguntasFiltradas.isEmpty()) {
            System.out.println("No hay preguntas disponibles para esta categoría.\n");
        }
    }

    public void seleccionarDificultad() {
        Scanner scanner = new Scanner(System.in);
        String dificultad = null;

        while (dificultad == null) {
            System.out.println("Selecciona la dificultad del juego:");
            System.out.println("1. Fácil");
            System.out.println("2. Intermedio");
            System.out.println("3. Difícil");
            System.out.print("Selecciona una opción: ");
            int dificultadSeleccionada = scanner.nextInt();
            scanner.nextLine();

            dificultad = switch (dificultadSeleccionada) {
                case 1 -> "fácil";
                case 2 -> "intermedio";
                case 3 -> "difícil";
                default -> {
                    System.out.println("Opción no válida. Por favor, selecciona una opción válida.\n");
                    yield null;
                }
            };
        }

        String finalDificultad = dificultad;

        preguntasFiltradas = preguntasFiltradas.stream()
                .filter(pregunta -> pregunta.getDificultad() != null && pregunta.getDificultad().equalsIgnoreCase(finalDificultad))
                .collect(Collectors.toList());

        if (preguntasFiltradas.isEmpty()) {
            System.out.println("No hay preguntas disponibles para esta dificultad.\n");
        } else {
            System.out.println("Dificultad seleccionada: " + dificultad);
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

            switch (pregunta.getTipoPregunta()) {
                case "input" -> manejarPreguntaTexto(scanner, pregunta);
                case "selección múltiple", "verdadero/falso" -> manejarPreguntaOpciones(scanner, pregunta);
                default -> System.out.println("Tipo de pregunta desconocido.\n");
            }
        }
    }

    private void manejarPreguntaTexto(Scanner scanner, Pregunta pregunta) {
        String respuesta;
        do {
            System.out.print("Escribe tu respuesta: ");
            respuesta = scanner.nextLine();
            if (!ValidadorRespuestas.validarRespuestaTexto(respuesta)) {
                System.out.println("Respuesta no válida. Por favor, ingresa un texto válido.");
            }
        } while (!ValidadorRespuestas.validarRespuestaTexto(respuesta));

        if (respuesta.equalsIgnoreCase(pregunta.getRespuestaCorrecta())) {
            System.out.println("¡Correcto!\n");
            puntaje.sumarPuntos(10);
        } else {
            System.out.println("Incorrecto. La respuesta correcta era: " + pregunta.getRespuestaCorrecta() + "\n");
        }
    }

    private void manejarPreguntaOpciones(Scanner scanner, Pregunta pregunta) {
        boolean respuestaValida = false;
        while (!respuestaValida) {
            for (int i = 0; i < pregunta.getOpciones().size(); i++) {
                System.out.println((i + 1) + ". " + pregunta.getOpciones().get(i));
            }

            System.out.print("Tu respuesta (número): ");
            int respuesta = scanner.nextInt();
            scanner.nextLine();

            if (respuesta >= 1 && respuesta <= pregunta.getOpciones().size()) {
                String opcionSeleccionada = pregunta.getOpciones().get(respuesta - 1);

                if (opcionSeleccionada.equalsIgnoreCase(pregunta.getRespuestaCorrecta())) {
                    System.out.println("¡Correcto!\n");
                    puntaje.sumarPuntos(10);
                } else {
                    System.out.println("Incorrecto. La respuesta correcta era: " + pregunta.getRespuestaCorrecta() + "\n");
                }

                respuestaValida = true;
            } else {
                System.out.println("Opción no válida. Por favor, selecciona una opción dentro del rango.\n");
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
