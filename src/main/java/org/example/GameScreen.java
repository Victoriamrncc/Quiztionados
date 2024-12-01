package org.example;

import javax.swing.*;
import java.awt.*;

public class GameScreen extends JPanel {
    private Juego juego;
    private JLabel questionLabel;
    private JPanel optionsPanel;
    private JLabel livesLabel;
    private JLabel scoreLabel;

    public GameScreen(QuiztionadosGUI gui, Juego juego) {
        this.juego = juego;
        setLayout(new BorderLayout());

        questionLabel = new JLabel("Pregunta aquí");
        optionsPanel = new JPanel();
        livesLabel = new JLabel("Vidas: 3");
        scoreLabel = new JLabel("Puntaje: 0");

        JButton backToMenu = new JButton("Volver al Menú");
        backToMenu.addActionListener(e -> gui.showScreen("Menu"));

        add(questionLabel, BorderLayout.NORTH);
        add(optionsPanel, BorderLayout.CENTER);
        add(livesLabel, BorderLayout.WEST);
        add(scoreLabel, BorderLayout.EAST);
        add(backToMenu, BorderLayout.SOUTH);

        mostrarPregunta();
    }

    private void mostrarPregunta() {
        Pregunta pregunta = juego.getPreguntasFiltradas().get(0); // Ejemplo
        questionLabel.setText(pregunta.getEnunciado());
        optionsPanel.removeAll();

        for (String opcion : pregunta.getOpciones()) {
            JButton opcionButton = new JButton(opcion);
            opcionButton.addActionListener(e -> validarRespuesta(opcion, pregunta));
            optionsPanel.add(opcionButton);
        }

        revalidate();
        repaint();
    }

    private void validarRespuesta(String respuesta, Pregunta pregunta) {
        if (respuesta.equalsIgnoreCase(pregunta.getRespuestaCorrecta())) {
            juego.puntaje.sumarPuntos(10);
        } else {
            juego.getVidas().perderVida(); // Se usa getVidas() en lugar de acceder directamente
        }

        livesLabel.setText("Vidas: " + juego.getVidas().getVidas());
        scoreLabel.setText("Puntaje: " + juego.puntaje.getPuntos());
    }
}
