package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Juego {
    private List<Pregunta> preguntas;
    private Puntaje puntaje;
    private List<Pregunta> preguntasFiltradas;
    private ScoreManager scoreManager;
    private Vidas vidas;

    public Juego() {
        this.puntaje = new Puntaje();
        this.scoreManager = new ScoreManager();
        this.vidas = new Vidas();
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

    public void seleccionarCategoria(String categoria) {
        if (categoria == null || categoria.isEmpty()) {
            System.out.println("No se seleccionó ninguna categoría.");
            return;
        }

        preguntasFiltradas = preguntas.stream()
                .filter(pregunta -> pregunta.getCategoria() != null &&
                        pregunta.getCategoria().trim().equalsIgnoreCase(categoria))
                .collect(Collectors.toList());

        if (preguntasFiltradas.isEmpty()) {
            System.out.println("No hay preguntas disponibles para esta categoría.\n");
        }
    }

    public void seleccionarDificultad(String dificultad) {
        if (dificultad == null || dificultad.isEmpty()) {
            System.out.println("No se seleccionó ninguna dificultad.");
            return;
        }

        preguntasFiltradas = preguntasFiltradas.stream()
                .filter(pregunta -> pregunta.getDificultad() != null &&
                        pregunta.getDificultad().equalsIgnoreCase(dificultad))
                .collect(Collectors.toList());

        if (preguntasFiltradas.isEmpty()) {
            System.out.println("No hay preguntas disponibles para esta dificultad.\n");
        }
    }

    public Pregunta obtenerSiguientePregunta() {
        if (preguntasFiltradas.isEmpty()) return null;
        return preguntasFiltradas.remove(0);
    }

    public boolean validarRespuesta(Pregunta pregunta, String respuesta) {
        boolean esCorrecta = respuesta.equalsIgnoreCase(pregunta.getRespuestaCorrecta());
        if (esCorrecta) {
            puntaje.sumarPuntosPorTipo(pregunta.getTipoPregunta(), true);
        } else {
            vidas.perderVida();
        }
        return esCorrecta;
    }

    public boolean juegoFinalizado() {
        return vidas.getVidas() <= 0 || preguntasFiltradas.isEmpty();
    }

    public void actualizarBestScore(String jugador) {
        scoreManager.actualizarBestScore(jugador, puntaje.getPuntos());
    }

    public int obtenerPuntaje() {
        return puntaje.getPuntos();
    }

    public int obtenerVidasRestantes() {
        return vidas.getVidas();
    }

    public Map<String, Integer> obtenerMejoresPuntajes() {
        return scoreManager.getBestScores();
    }
}


