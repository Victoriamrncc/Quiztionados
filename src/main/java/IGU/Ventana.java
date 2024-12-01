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
        menu.mostrarMenu(); // Inicia el flujo del juego desde el menú
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
