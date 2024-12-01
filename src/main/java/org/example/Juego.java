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
    private ScoreManager scoreManager;
    private Vidas v = new Vidas();

    public Juego() {
        this.puntaje = new Puntaje();
        this.scoreManager = new ScoreManager();
    }

    public void mostrarBestScores() {
        scoreManager.mostrarBestScores();
    }

    public void jugar(String playerName) {
        if (preguntasFiltradas == null || preguntasFiltradas.isEmpty()) {
            System.out.println("No hay preguntas cargadas.\n");
            return;
        }

        Scanner scanner = new Scanner(System.in);

        for (Pregunta pregunta : preguntasFiltradas) {
            if (v.getVidas() <= 0) {
                System.out.println("¡Se acabaron tus vidas! Fin del juego.\n");
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
        scoreManager.actualizarBestScore(playerName, puntaje.getPuntos());
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
            for (int i = 0; i < pregunta.getOpciones().size(); i++) {
                System.out.println((i + 1) + ". " + pregunta.getOpciones().get(i));
            }

            System.out.print("Tu respuesta (número): ");
            int respuesta = scanner.nextInt();
            scanner.nextLine();

            if (ValidadorRespuestas.validarRespuestaSeleccion(respuesta, pregunta.getOpciones().size())) {
                String opcionSeleccionada = pregunta.getOpciones().get(respuesta - 1);
                boolean esCorrecta = opcionSeleccionada.equalsIgnoreCase(pregunta.getRespuestaCorrecta());

                if (esCorrecta) {
                    System.out.println("¡Correcto!\n");
                    puntaje.sumarPuntosPorTipo(pregunta.getTipoPregunta(), true);
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
        Puntaje puntajeCargado = Puntaje.cargarPuntaje(archivo);
        if (puntajeCargado != null) {
            this.puntaje = puntajeCargado;
        }
    }
}

