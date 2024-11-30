package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Juego {
    protected List<Pregunta> preguntas;
    protected Puntaje puntaje;
    protected List<Pregunta> preguntasFiltradas;
    private Map<String, Integer> bestScores;
    private static String scoreFile = "src/main/resources/scores.json";

    public Juego() {
        this.puntaje = new Puntaje();
        bestScores = new HashMap<>();
        cargarBestScores();
    }

    private void cargarBestScores() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            File file = new File(scoreFile);
            if (file.exists()) {
                bestScores = mapper.readValue(file, new TypeReference<Map<String, Integer>>() {});
            }
        } catch (IOException e) {
            System.out.println("No se pudieron cargar los mejores puntajes: " + e.getMessage());
        }
    }

    private void guardarBestScores() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(scoreFile), bestScores);
        } catch (IOException e) {
            System.out.println("No se pudieron guardar los mejores puntajes: " + e.getMessage());
        }
    }

    private void actualizarBestScore(String playerName, int score) {
        int maxScore = Math.max(bestScores.getOrDefault(playerName, 0), score);
        bestScores.put(playerName, maxScore);
        guardarBestScores();
    }

    public void mostrarBestScores() {
        System.out.println("=== Mejores puntajes ===");
        if (bestScores.isEmpty()) {
            System.out.println("No hay puntajes registrados.");
        } else {
            bestScores.forEach((name, score) -> System.out.println(name + ": " + score));
        }
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

    public void seleccionarCategoria() { //menu
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

        System.out.println("Preguntas disponibles: " + preguntasFiltradas.size());
    }

    public void seleccionarDificultad() { //menu
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

    Vidas v = new Vidas();

    public void jugar(String playerName) {
        if (preguntasFiltradas == null || preguntasFiltradas.isEmpty()) {
            System.out.println("No hay preguntas cargadas.\n");
            return;
        }

        Scanner scanner = new Scanner(System.in);

        for (Pregunta pregunta : preguntasFiltradas) {
            if (v.getVidas() <= 0) {
                System.out.println("¡Se acabaron tus vidas! Fin del juego.\n");
                /*v.setVidas(3);*/
                break;
            }

            System.out.println(pregunta.getEnunciado());

            switch (pregunta.getTipoPregunta()) {
                case "input" -> manejarPreguntaTexto(scanner, pregunta);
                case "selección múltiple", "verdadero/falso" -> manejarPreguntaOpciones(scanner, pregunta);
                default -> System.out.println("Tipo de pregunta desconocido.\n");
            }
        }

        if (v.getVidas() > 0) {
            System.out.println("¡Has completado todas las preguntas de esta categoría y dificultad!");
        }
        actualizarBestScore(playerName, puntaje.getPuntos());
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
            puntaje.sumarPuntos(15);
        } else {
            System.out.println("Incorrecto. La respuesta correcta era: " + pregunta.getRespuestaCorrecta() + "\n");
            v.perderVida();
        }
    }

    private void manejarPreguntaOpciones(Scanner scanner, Pregunta pregunta) {
        boolean respuestaValida = false;
        while (!respuestaValida) {
            // Mostrar las opciones
            for (int i = 0; i < pregunta.getOpciones().size(); i++) {
                System.out.println((i + 1) + ". " + pregunta.getOpciones().get(i));
            }

            System.out.print("Tu respuesta (número): ");
            int respuesta = scanner.nextInt();
            scanner.nextLine();

            // Validar la respuesta seleccionada
            if (ValidadorRespuestas.validarRespuestaSeleccion(respuesta, pregunta.getOpciones().size())) {
                String opcionSeleccionada = pregunta.getOpciones().get(respuesta - 1);
                boolean esCorrecta = opcionSeleccionada.equalsIgnoreCase(pregunta.getRespuestaCorrecta());

                if (esCorrecta) {
                    System.out.println("¡Correcto!\n");
                    puntaje.sumarPuntosPorTipo(pregunta.getTipoPregunta(), true); // Pasar tipo y resultado
                } else {
                    System.out.println("Incorrecto. La respuesta correcta era: " + pregunta.getRespuestaCorrecta() + "\n");
                    v.perderVida();
                }

                respuestaValida = true;
            } else {
                System.out.println("Opción no válida. Por favor, selecciona una opción dentro del rango.\n");
            }
        }
    }


    public void guardarPuntaje(String archivo) {
        puntaje.guardarPuntaje(archivo);
    }

    public void cargarPuntaje(String archivo) {
        Puntaje cargado = Puntaje.cargarPuntaje(archivo);
        if (cargado != null) {
            this.puntaje = cargado;
        }
    }
}
