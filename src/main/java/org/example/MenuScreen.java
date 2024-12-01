package org.example;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuScreen extends JPanel {
    public MenuScreen(QuiztionadosGUI gui, Juego juego) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JButton startGameButton = new JButton("Iniciar Juego");
        JButton viewScoresButton = new JButton("Ver Mejores Puntajes");
        JButton exitButton = new JButton("Salir");

        startGameButton.addActionListener(e -> gui.showScreen("Game"));
        viewScoresButton.addActionListener(e -> gui.showScreen("Scores"));
        exitButton.addActionListener(e -> System.exit(0));

        add(startGameButton);
        add(viewScoresButton);
        add(exitButton);
    }
}
