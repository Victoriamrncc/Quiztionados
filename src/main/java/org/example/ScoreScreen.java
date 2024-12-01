package org.example;
import javax.swing.*;

public class ScoreScreen extends JPanel {
    public ScoreScreen(QuiztionadosGUI gui, Juego juego) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel("Mejores Puntajes");
        add(titleLabel);

        juego.getBestScores().forEach((name, score) -> {
            add(new JLabel(name + ": " + score));
        });

        JButton backToMenu = new JButton("Volver al Menú");
        backToMenu.addActionListener(e -> gui.showScreen("Menu"));
        add(backToMenu);
    }
}
