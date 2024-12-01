package controlador;

import org.example.Juego;
import org.example.ScoreManager;
import org.example.Pregunta;

import java.util.Map;

public class ControladorJuego {
    private Juego juego;
    private ScoreManager scoreManager;

    public ControladorJuego() {
        juego = new Juego();
        juego.cargarPreguntas("preguntas.json");
        scoreManager = new ScoreManager();
    }

    public Map<String, Integer> obtenerMejoresPuntajes() {
        return scoreManager.getBestScores();
    }

    public void seleccionarCategoria(String categoria) {
        juego.seleccionarCategoria(categoria);
    }

    public void seleccionarDificultad(String dificultad) {
        juego.seleccionarDificultad(dificultad);
    }

    public Pregunta obtenerSiguientePregunta() {
        if (juego.getPreguntasFiltradas().isEmpty()) return null;
        return juego.getPreguntasFiltradas().remove(0);
    }

    public boolean validarRespuesta(Pregunta pregunta, String respuesta) {
        boolean esCorrecta = respuesta.equalsIgnoreCase(pregunta.getRespuestaCorrecta());
        if (esCorrecta) {
            juego.getPuntaje().sumarPuntosPorTipo(pregunta.getTipoPregunta(), true);
        } else {
            juego.getVidas().perderVida();
        }
        return esCorrecta;
    }

    public boolean juegoFinalizado() {
        return juego.getVidas().getVidas() <= 0 || juego.getPreguntasFiltradas().isEmpty();
    }

    public int obtenerPuntaje() {
        return juego.getPuntaje().getPuntos();
    }

//    public int obtenerVidasRestantes() {
//        return juego.getVidas().getVidas();
//    }
}
