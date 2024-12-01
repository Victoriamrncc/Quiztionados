package IGU;

import org.example.Juego;
import org.example.Pregunta;

import javax.swing.*;
import java.awt.*;

public class Ventana {
    private JFrame frame;
    private JButton button1, button2, button3; // Botones
    private Juego juego;

    public Ventana() {
        // Inicializar frame
        frame = new JFrame("Trivia Game");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(3, 1)); // Una columna, tres filas

        // Inicializar juego
        juego = new Juego();
        juego.cargarPreguntas("preguntas.json");

        // Inicializar botones con acciones
        button1 = crearBoton("Iniciar Juego", e -> iniciarJuego());
        button2 = crearBoton("Mostrar Puntajes", e -> mostrarPuntajes());
        button3 = crearBoton("Salir", e -> salir());

        // Añadir botones al frame
        frame.add(button1);
        frame.add(button2);
        frame.add(button3);

        // Mostrar frame
        frame.setVisible(true);
    }

    // Método para crear botones con menos redundancia
    private JButton crearBoton(String texto, java.awt.event.ActionListener accion) {
        JButton boton = new JButton(texto);
        boton.addActionListener(accion);
        return boton;
    }

    private void iniciarJuego() {
        String playerName = pedirEntrada("Ingrese su nombre:", "Nombre del jugador");
        if (playerName == null) return;

        String categoria = seleccionarOpcion(new String[]{"Historia", "Ciencia", "Geografía", "Películas", "Música"}, "Seleccione una categoría");
        if (categoria == null) return;

        String dificultad = seleccionarOpcion(new String[]{"Fácil", "Intermedio", "Difícil"}, "Seleccione una dificultad");
        if (dificultad == null) return;

        // Configurar juego
        juego.seleccionarCategoria(categoria);
        juego.seleccionarDificultad(dificultad);

        // Mostrar la primera pregunta
        siguientePregunta(playerName);
    }

    // Método genérico para solicitar entradas de texto
    private String pedirEntrada(String mensaje, String titulo) {
        JPanel panel = new JPanel(new GridLayout(2, 1));
        JLabel label = new JLabel(mensaje);
        JTextField textField = new JTextField();

        panel.add(label);
        panel.add(textField);

        int option = JOptionPane.showConfirmDialog(
                frame, panel, titulo, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            String input = textField.getText().trim();
            if (!input.isEmpty()) {
                return input;
            } else {
                JOptionPane.showMessageDialog(frame, "El campo no puede estar vacío.");
                return pedirEntrada(mensaje, titulo); // Reintenta si está vacío.
            }
        }
        return null; // Usuario canceló.
    }

    // Método genérico para seleccionar opciones
    private String seleccionarOpcion(String[] opciones, String titulo) {
        JComboBox<String> comboBox = new JComboBox<>(opciones);

        int option = JOptionPane.showConfirmDialog(
                frame, comboBox, titulo, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        return option == JOptionPane.OK_OPTION ? (String) comboBox.getSelectedItem() : null;
    }

    private void mostrarPuntajes() {
        var bestScores = juego.obtenerMejoresPuntajes();

        String mensaje = bestScores.isEmpty()
                ? "No hay puntajes registrados."
                : "=== Mejores Puntajes ===\n" + bestScores.entrySet().stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .reduce("", (a, b) -> a + b + "\n");

        JOptionPane.showMessageDialog(frame, mensaje, "Mejores Puntajes", JOptionPane.INFORMATION_MESSAGE);
    }

    private void salir() {
        int confirm = JOptionPane.showConfirmDialog(frame, "¿Estás seguro de que quieres salir?",
                "Confirmar salida", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private void siguientePregunta(String playerName) {
        if (juego.juegoFinalizado()) {
            JOptionPane.showMessageDialog(frame, "¡Fin del juego! Puntaje final: " + juego.obtenerPuntaje());
            juego.actualizarBestScore(playerName);
            mostrarMenuPrincipal();
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

        JPanel opcionesPanel = new JPanel();
        ButtonGroup grupoOpciones = new ButtonGroup();

        for (String opcion : pregunta.getOpciones()) {
            JRadioButton botonOpcion = new JRadioButton(opcion);
            botonOpcion.setActionCommand(opcion);
            grupoOpciones.add(botonOpcion);
            opcionesPanel.add(botonOpcion);
        }
        panelPregunta.add(opcionesPanel, BorderLayout.CENTER);

        JButton botonResponder = crearBoton("Responder", e -> {
            if (grupoOpciones.getSelection() == null) {
                JOptionPane.showMessageDialog(frame, "Debes seleccionar una respuesta.");
                return;
            }

            String seleccion = grupoOpciones.getSelection().getActionCommand();
            boolean esCorrecta = juego.validarRespuesta(pregunta, seleccion);

            JOptionPane.showMessageDialog(frame, esCorrecta ? "¡Correcto!" : "Incorrecto. La respuesta correcta era: " + pregunta.getRespuestaCorrecta());
            siguientePregunta(playerName);
        });

        panelPregunta.add(botonResponder, BorderLayout.SOUTH);

        frame.getContentPane().removeAll();
        frame.add(panelPregunta);
        frame.revalidate();
        frame.repaint();
    }

    private void mostrarMenuPrincipal() {
        frame.getContentPane().removeAll();
        frame.setLayout(new GridLayout(3, 1));
        frame.add(button1);
        frame.add(button2);
        frame.add(button3);
        frame.revalidate();
        frame.repaint();
    }
}
