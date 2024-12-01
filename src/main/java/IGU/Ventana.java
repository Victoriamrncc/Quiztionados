package IGU;

import controlador.ControladorJuego;
import org.example.Pregunta;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class Ventana {
    private JFrame frame;
    private JButton button1; // Iniciar Juego
    private JButton button2; // Mostrar Puntajes
    private JButton button3; // Salir
    private ControladorJuego controlador;

    public Ventana() {
        controlador = new ControladorJuego();

        // Inicializamos el frame
        frame = new JFrame("Trivia Game");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(3, 1)); // Una columna, tres filas

        // Inicializamos los botones
        button1 = new JButton("Iniciar Juego");
        button2 = new JButton("Mostrar Puntajes");
        button3 = new JButton("Salir");

        // Añadimos acciones a los botones
        button1.addActionListener(e -> iniciarJuego());
        button2.addActionListener(e -> mostrarPuntajes());
        button3.addActionListener(e -> salir());

        // Añadimos los botones al frame
        frame.add(button1);
        frame.add(button2);
        frame.add(button3);

        // Hacemos visible el frame
        frame.setVisible(true);
    }

    private void iniciarJuego() {
        String playerName = pedirNombreJugador();
        if (playerName == null) return;

        String categoria = seleccionarCategoria();
        if (categoria == null) return;

        String dificultad = seleccionarDificultad();
        if (dificultad == null) return;

        controlador.seleccionarCategoria(categoria);
        controlador.seleccionarDificultad(dificultad);

        siguientePregunta();
    }

    private String pedirNombreJugador() {
        String nombre = JOptionPane.showInputDialog(frame, "Ingrese su nombre:", "Nombre del jugador", JOptionPane.PLAIN_MESSAGE);
        return (nombre != null && !nombre.trim().isEmpty()) ? nombre.trim() : null;
    }

    private String seleccionarCategoria() {
        String[] categorias = {"Historia", "Ciencia", "Geografía", "Películas", "Música"};
        return (String) JOptionPane.showInputDialog(frame, "Seleccione una categoría", "Categoría",
                JOptionPane.PLAIN_MESSAGE, null, categorias, categorias[0]);
    }

    private String seleccionarDificultad() {
        String[] dificultades = {"Fácil", "Intermedio", "Difícil"};
        return (String) JOptionPane.showInputDialog(frame, "Seleccione una dificultad", "Dificultad",
                JOptionPane.PLAIN_MESSAGE, null, dificultades, dificultades[0]);
    }

    private void mostrarPuntajes() {
        Map<String, Integer> bestScores = controlador.obtenerMejoresPuntajes();
        if (bestScores.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No hay puntajes registrados.", "Mejores Puntajes", JOptionPane.INFORMATION_MESSAGE);
        } else {
            StringBuilder sb = new StringBuilder("=== Mejores Puntajes ===\n");
            bestScores.forEach((name, score) -> sb.append(name).append(": ").append(score).append("\n"));
            JOptionPane.showMessageDialog(frame, sb.toString(), "Mejores Puntajes", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void salir() {
        int confirm = JOptionPane.showConfirmDialog(frame, "¿Estás seguro de que quieres salir?", "Confirmar salida", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private void siguientePregunta() {
        if (controlador.juegoFinalizado()) {
            JOptionPane.showMessageDialog(frame, "¡Juego finalizado! Puntaje final: " + controlador.obtenerPuntaje());
            return;
        }

        Pregunta pregunta = controlador.obtenerSiguientePregunta();
        if (pregunta == null) {
            JOptionPane.showMessageDialog(frame, "¡Has respondido todas las preguntas! Puntaje final: " + controlador.obtenerPuntaje());
            return;
        }

        mostrarPregunta(pregunta);
    }

    private void mostrarPregunta(Pregunta pregunta) {
        JPanel panelPregunta = new JPanel(new BorderLayout());
        JLabel enunciado = new JLabel("<html>" + pregunta.getEnunciado() + "</html>");
        enunciado.setHorizontalAlignment(SwingConstants.CENTER);
        panelPregunta.add(enunciado, BorderLayout.NORTH);

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
                JOptionPane.showMessageDialog(frame, "Debes seleccionar una respuesta.");
                return;
            }

            String seleccion = grupoOpciones.getSelection().getActionCommand();
            boolean esCorrecta = controlador.validarRespuesta(pregunta, seleccion);

            JOptionPane.showMessageDialog(frame, esCorrecta ? "¡Correcto!" : "Incorrecto. La respuesta correcta era: " + pregunta.getRespuestaCorrecta());
            siguientePregunta();
        });

        panelPregunta.add(botonResponder, BorderLayout.SOUTH);

        frame.getContentPane().removeAll();
        frame.add(panelPregunta);
        frame.revalidate();
        frame.repaint();
    }
}

