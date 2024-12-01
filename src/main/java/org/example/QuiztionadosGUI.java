package org.example;
import javax.swing.*;
import java.awt.*;

public class QuiztionadosGUI {
    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private Juego juego;

    public QuiztionadosGUI() {
        juego = new Juego();
        frame = new JFrame("Quiztionados");
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Cargar preguntas al iniciar
        juego.cargarPreguntas("preguntas.json");

        // Configurar pantallas
        mainPanel.add(new MenuScreen(this, juego), "Menu");
        mainPanel.add(new GameScreen(this, juego), "Game");
        mainPanel.add(new ScoreScreen(this, juego), "Scores");

        frame.add(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setVisible(true);
    }

    public void showScreen(String screenName) {
        cardLayout.show(mainPanel, screenName);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(QuiztionadosGUI::new);
    }
}
