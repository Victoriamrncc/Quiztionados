package IGU;

import javax.swing.*;
import java.awt.*;
import org.example.ControladorJuego;

public class Ventana {
    private JFrame frame;
    private JButton button1, button2, button3;
    private ControladorJuego controlador;

    public Ventana() {
        // Inicializar frame
        frame = new JFrame("Trivia Game");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(3, 1));

        // Inicializar controlador del juego
        controlador = new ControladorJuego(this);

        // Inicializar botones con acciones
        button1 = crearBoton("Iniciar Juego", e -> controlador.iniciarJuego());
        button2 = crearBoton("Mostrar Puntajes", e -> controlador.mostrarPuntajes());
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

    private void salir() {
        int confirm = JOptionPane.showConfirmDialog(frame, "¿Estás seguro de que quieres salir?",
                "Confirmar salida", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    // Métodos auxiliares para interactuar con el usuario
    public String pedirEntrada(String mensaje, String titulo) {
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
                return pedirEntrada(mensaje, titulo);
            }
        }
        return null;
    }

    public String seleccionarOpcion(String[] opciones, String titulo) {
        JComboBox<String> comboBox = new JComboBox<>(opciones);

        int option = JOptionPane.showConfirmDialog(
                frame, comboBox, titulo, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        return option == JOptionPane.OK_OPTION ? (String) comboBox.getSelectedItem() : null;
    }

    public void mostrarMensaje(String mensaje, String titulo) {
        JOptionPane.showMessageDialog(frame, mensaje, titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    public void actualizarPanel(JPanel nuevoPanel) {
        frame.getContentPane().removeAll();
        frame.add(nuevoPanel);
        frame.revalidate();
        frame.repaint();
    }

    public void mostrarMenuPrincipal() {
        frame.getContentPane().removeAll();
        frame.setLayout(new GridLayout(3, 1));
        frame.add(button1);
        frame.add(button2);
        frame.add(button3);
        frame.revalidate();
        frame.repaint();
    }
}
