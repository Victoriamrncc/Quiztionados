package org.example;

import org.example.Juego;

import java.util.concurrent.*;

public class Temporizador {
    private ScheduledExecutorService timer;
    private boolean respuestaRecibida;
    private int tiempoLimite;
    private int tiempoRestante;

    public void configurarTiempoPorDificultad(String dificultad) {
        switch (dificultad.toLowerCase()) {
            case "fácil" -> tiempoLimite = 30;
            case "intermedio" -> tiempoLimite = 20;
            case "difícil" -> tiempoLimite = 10;
            default -> tiempoLimite = 30;
        }
        tiempoRestante = tiempoLimite;
    }

    public boolean iniciarTemporizador() {
        respuestaRecibida = false;
        tiempoRestante = tiempoLimite;

        timer = Executors.newSingleThreadScheduledExecutor();

        timer.scheduleAtFixedRate(() -> {
            if (tiempoRestante > 0 && !respuestaRecibida) {
                System.out.println("Tiempo restante: " + tiempoRestante + " segundos.");
                tiempoRestante--;
            }
        }, 0, 1, TimeUnit.SECONDS);

        timer.schedule(() -> {
            if (!respuestaRecibida) {
                System.out.println("\n¡Tiempo agotado! Respuesta incorrecta.");
                perderVida();
            }
        }, tiempoLimite, TimeUnit.SECONDS);
        return true;
    }

    public void detenerTemporizador() {
        respuestaRecibida = true;
        if (timer != null) {
            timer.shutdownNow();
        }
    }
    public void perderVida() {
        System.out.println("Has perdido una vida.");
    }
}