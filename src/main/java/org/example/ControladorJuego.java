package org.example;

import IGU.Ventana;
import javax.swing.*;
import java.awt.*;

public class ControladorJuego {
    private Juego juego;
    private Ventana ventana;

    public ControladorJuego(Ventana ventana) {
        this.ventana = ventana;
        this.juego = new Juego();
        this.juego.cargarPreguntas("preguntas.json");
    }

    public void iniciarJuego() {
        String playerName = ventana.pedirEntrada("Ingrese su nombre:", "Nombre del jugador");
        if (playerName == null) return;

        String categoria = ventana.seleccionarOpcion(new String[]{"Historia", "Ciencia", "Geografía", "Películas", "Música"}, "Seleccione una categoría");
        if (categoria == null) return;

        String dificultad = ventana.seleccionarOpcion(new String[]{"Fácil", "Intermedio", "Difícil"}, "Seleccione una dificultad");
        if (dificultad == null) return;

        juego.configurarJuego(categoria, dificultad);

        siguientePregunta(playerName);
    }

    public void mostrarPuntajes() {
        var bestScores = juego.obtenerMejoresPuntajes();
        String mensaje = bestScores.isEmpty()
                ? "No hay puntajes registrados."
                : "=== Mejores Puntajes ===\n" + bestScores.entrySet().stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .reduce("", (a, b) -> a + b + "\n");

        ventana.mostrarMensaje(mensaje, "Mejores Puntajes");
    }

    private void siguientePregunta(String playerName) {
        if (juego.juegoFinalizado()) {

            juego.actualizarBestScore(playerName);
            JPanel panelFinJuego = new JPanel(new BorderLayout());

            JLabel mensajeFinal = new JLabel("<html>¡Fin del juego!<br>Puntaje final: " + juego.obtenerPuntaje() + "</html>");
            mensajeFinal.setHorizontalAlignment(SwingConstants.CENTER);
            panelFinJuego.add(mensajeFinal, BorderLayout.CENTER);


            JPanel botonesPanel = new JPanel();
            JButton botonReiniciar = new JButton("Volver a jugar");
            JButton botonSalir = new JButton("Salir");

            botonReiniciar.addActionListener(e -> {
                juego.reiniciarJuego();
                ventana.mostrarMenuPrincipal();
            });

            botonSalir.addActionListener(e -> System.exit(0));

            botonesPanel.add(botonReiniciar);
            botonesPanel.add(botonSalir);

            panelFinJuego.add(botonesPanel, BorderLayout.SOUTH);

            ventana.actualizarPanel(panelFinJuego);
        } else {
            Pregunta siguiente = juego.obtenerSiguientePregunta();
            if (siguiente != null) {
                mostrarPregunta(siguiente, playerName);
            }
        }
    }

    private void mostrarPregunta(Pregunta pregunta, String playerName) {
        JPanel panelPregunta = new JPanel(new BorderLayout());

        JLabel enunciado = new JLabel("<html>" + pregunta.getEnunciado() + "</html>");
        enunciado.setHorizontalAlignment(SwingConstants.CENTER);
        panelPregunta.add(enunciado, BorderLayout.NORTH);
        JLabel vidasRestantes = new JLabel("Vidas restantes: " + juego.obtenerVidasRestantes());
        vidasRestantes.setHorizontalAlignment(SwingConstants.CENTER);

        if (pregunta.getTipoPregunta().equalsIgnoreCase("input")) {

            JTextField campoTexto = new JTextField();
            panelPregunta.add(campoTexto, BorderLayout.CENTER);

            JButton botonResponder = new JButton("Responder");
            botonResponder.addActionListener(e -> {
                String respuesta = campoTexto.getText().trim();
                if (respuesta.isEmpty()) {
                    ventana.mostrarMensaje("Por favor, escribe una respuesta.", "Advertencia");
                    return;
                }

                boolean esCorrecta = juego.validarRespuesta(pregunta, respuesta);

                ventana.mostrarMensaje(
                        esCorrecta ? "¡Correcto!" : "Incorrecto. La respuesta correcta era: " + pregunta.getRespuestaCorrecta(),
                        "Resultado"
                );
                siguientePregunta(playerName);
            });

            panelPregunta.add(botonResponder, BorderLayout.SOUTH);

        } else {

            JPanel opcionesPanel = new JPanel();
            ButtonGroup grupoOpciones = new ButtonGroup();

            for (String opcion : pregunta.getOpciones()) {
                JRadioButton botonOpcion = new JRadioButton(opcion);
                botonOpcion.setActionCommand(opcion);
                grupoOpciones.add(botonOpcion);
                opcionesPanel.add(botonOpcion);
            }
            panelPregunta.add(opcionesPanel, BorderLayout.CENTER);

            JButton botonResponder = new JButton("Responder");
            botonResponder.addActionListener(e -> {
                if (grupoOpciones.getSelection() == null) {
                    ventana.mostrarMensaje("Debes seleccionar una respuesta.", "Advertencia");
                    return;
                }

                String seleccion = grupoOpciones.getSelection().getActionCommand();
                boolean esCorrecta = juego.validarRespuesta(pregunta, seleccion);

                ventana.mostrarMensaje(
                        esCorrecta ? "¡Correcto!" : "Incorrecto. La respuesta correcta era: " + pregunta.getRespuestaCorrecta(),
                        "Resultado"
                );
                siguientePregunta(playerName);
            });

            JPanel panelInferior = new JPanel(new BorderLayout());
            panelInferior.add(vidasRestantes, BorderLayout.NORTH);
            panelInferior.add(botonResponder, BorderLayout.SOUTH);
            panelPregunta.add(panelInferior, BorderLayout.SOUTH);
        }

        ventana.actualizarPanel(panelPregunta);
    }

}