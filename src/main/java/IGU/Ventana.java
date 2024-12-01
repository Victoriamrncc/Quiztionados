package IGU;

import org.example.Juego;
import org.example.Menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Ventana {
    private JFrame frame;
    private JButton button1; // Iniciar Juego
    private JButton button2; // Mostrar Puntajes
    private JButton button3; // Salir
    private Juego juego;
    private Menu menu;

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
        if (playerName == null) return; // Si el usuario cancela.

        String categoria = seleccionarCategoria();
        if (categoria == null) return; // Si el usuario cancela.

        String dificultad = seleccionarDificultad();
        if (dificultad == null) return; // Si el usuario cancela.

        System.out.println("Iniciando el juego con:");
        System.out.println("Jugador: " + playerName);
        System.out.println("Categoría: " + categoria);
        System.out.println("Dificultad: " + dificultad);

        juego.jugar(playerName); // Llama al flujo del juego.
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
            return (String) comboBox.getSelectedItem();
        }
        return null; // Usuario canceló.
    }

    private String seleccionarDificultad() {
        String[] dificultades = {"Fácil", "Intermedio", "Difícil"};
        JComboBox<String> comboBox = new JComboBox<>(dificultades);

        int option = JOptionPane.showConfirmDialog(
                frame, comboBox, "Seleccione una dificultad", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            return (String) comboBox.getSelectedItem();
        }
        return null; // Usuario canceló.
    }

    private void mostrarPuntajes() {
        JOptionPane.showMessageDialog(frame, "=== Mejores Puntajes ===\n" +
                String.join("\n", juego.getScoreManager().getBestScores().entrySet().stream()
                        .map(entry -> entry.getKey() + ": " + entry.getValue())
                        .toList()));
    }

    private void salir() {
        int confirm = JOptionPane.showConfirmDialog(frame, "¿Estás seguro de que quieres salir?",
                "Confirmar salida", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}
