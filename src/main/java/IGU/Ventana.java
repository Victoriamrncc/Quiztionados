package IGU;

import org.example.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import org.example.Menu;

public class Ventana {
    private JFrame frame;
    private JButton button1; // Iniciar Juego
    private JButton button2; // Mostrar Puntajes
    private JButton button3; // Salir
    private Juego juego;
    private Menu menu;
    private ScoreManager scoreManager;

    public Ventana() {
        // Inicializamos el frame
        frame = new JFrame("Trivia Game");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(3, 1)); // Una columna, tres filas

        // Inicializamos los botones
        button1 = new JButton("Iniciar Juego");
        button2 = new JButton("Mostrar Puntajes");
        button3 = new JButton("Salir");

        // Inicializamos el juego y el menú
        juego = new Juego();
        juego.cargarPreguntas("preguntas.json");
        menu = new Menu(juego);
        scoreManager = new ScoreManager();

        // Añadimos acciones a los botones
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iniciarJuego();
            }
        });

        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarPuntajes();
            }
        });

        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salir();
            }
        });

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

        // Configura el juego
        menu.seleccionarCategoria(categoria);
        menu.seleccionarDificultad(dificultad);

        // Muestra la primera pregunta
        siguientePregunta();
    }

    private String pedirNombreJugador() {
        JPanel panel = new JPanel(new GridLayout(2, 1));
        JLabel label = new JLabel("Ingrese su nombre:");
        JTextField textField = new JTextField();

        panel.add(label);
        panel.add(textField);

        int option = JOptionPane.showConfirmDialog(
                frame, panel, "Nombre del jugador", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            String playerName = textField.getText().trim();
            if (!playerName.isEmpty()) {
                return playerName;
            } else {
                JOptionPane.showMessageDialog(frame, "El nombre no puede estar vacío.");
                return pedirNombreJugador(); // Reintenta si está vacío.
            }
        }
        return null; // Usuario canceló.
    }

    private String seleccionarCategoria() {
        String[] categorias = {"Historia", "Ciencia", "Geografía", "Películas", "Música"};
        JComboBox<String> comboBox = new JComboBox<>(categorias);

        int option = JOptionPane.showConfirmDialog(
                frame, comboBox, "Seleccione una categoría", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            String categoriaSeleccionada = (String) comboBox.getSelectedItem();
            menu.seleccionarCategoria(categoriaSeleccionada);
            return categoriaSeleccionada;
        }
        return null; // Usuario canceló.
    }

    private String seleccionarDificultad() {
        String[] dificultades = {"Fácil", "Intermedio", "Difícil"};
        JComboBox<String> comboBox = new JComboBox<>(dificultades);

        int option = JOptionPane.showConfirmDialog(
                frame, comboBox, "Seleccione una dificultad", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            String dificultadSeleccionada = (String) comboBox.getSelectedItem();
            menu.seleccionarDificultad(dificultadSeleccionada);
            return dificultadSeleccionada;
        }
        return null;
    }

    private void mostrarPuntajes() {
        Map<String, Integer> bestScores = scoreManager.getBestScores();

        if (bestScores.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No hay puntajes registrados.",
                    "Mejores Puntajes", JOptionPane.INFORMATION_MESSAGE);
        } else {
            StringBuilder sb = new StringBuilder("=== Mejores Puntajes ===\n");
            bestScores.forEach((name, score) -> sb.append(name).append(": ").append(score).append("\n"));
            JOptionPane.showMessageDialog(frame, sb.toString(),
                    "Mejores Puntajes", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void salir() {
        int confirm = JOptionPane.showConfirmDialog(frame, "¿Estás seguro de que quieres salir?",
                "Confirmar salida", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
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

    private void siguientePregunta() {
        if (juego.getVidas().getVidas() <= 0) {
            JOptionPane.showMessageDialog(frame, "¡Te has quedado sin vidas! Fin del juego.");
            mostrarMenuPrincipal();
        } else if (juego.getPreguntasFiltradas().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "¡Has respondido todas las preguntas! Puntaje final: " + juego.getPuntaje().getPuntos());
            mostrarMenuPrincipal();
        } else {
            Pregunta siguiente = juego.getPreguntasFiltradas().remove(0);
            mostrarPregunta(siguiente);
        }
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
            botonOpcion.setActionCommand(opcion); // Asigna la opción como identificador único
            grupoOpciones.add(botonOpcion);
            opcionesPanel.add(botonOpcion);
        }
        panelPregunta.add(opcionesPanel, BorderLayout.CENTER);

        JButton botonResponder = new JButton("Responder");
        botonResponder.addActionListener(e -> {
            // Verificar selección
            if (grupoOpciones.getSelection() == null) {
                JOptionPane.showMessageDialog(frame, "Debes seleccionar una respuesta.");
                return; // No avanza hasta que se seleccione algo
            }

            // Obtener la opción seleccionada
            String seleccion = grupoOpciones.getSelection().getActionCommand();

            if (seleccion.equalsIgnoreCase(pregunta.getRespuestaCorrecta())) {
                JOptionPane.showMessageDialog(frame, "¡Correcto!");
                juego.getPuntaje().sumarPuntosPorTipo(pregunta.getTipoPregunta(), true);
            } else {
                JOptionPane.showMessageDialog(frame, "Incorrecto. La respuesta correcta era: " + pregunta.getRespuestaCorrecta());
                juego.getVidas().perderVida();
            }

            // Avanzar a la siguiente pregunta
            siguientePregunta();
        });

        panelPregunta.add(botonResponder, BorderLayout.SOUTH);

        frame.getContentPane().removeAll();
        frame.add(panelPregunta);
        frame.revalidate();
        frame.repaint();
    }

}
