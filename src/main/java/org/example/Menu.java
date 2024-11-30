package org.example;

import java.util.Scanner;

public class Menu {
    private Juego juego;
    private final Scanner scanner;

    public Menu(Juego juego) {
        this.juego = juego;
        this.scanner = new Scanner(System.in);
    }

    public void mostrarMenu() {
        boolean continuar = true;

        while (continuar) {
            System.out.println("=== MENÚ DEL JUEGO ===");
            System.out.println("1. Iniciar Juego");
            System.out.println("2. Ver Mejores Puntajes");
            System.out.println("3. Salir");
            System.out.print("Selecciona una opción: ");

            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1 -> iniciarJuego();
                case 2 -> mostrarBestScores();
                case 3 -> {
                    System.out.println("¡Gracias por jugar!");
                    continuar = false;
                }
                default -> System.out.println("Opción no válida.");
            }
        }
    }

    private void iniciarJuego() {
        boolean volverAJugar;
        do {
            System.out.println("Por favor, ingresa tu nombre: ");
            String playerName = scanner.nextLine(); // Obtener el nombre del jugador

            juego.seleccionarCategoria();
            juego.seleccionarDificultad();

            System.out.println("Iniciando el juego con preguntas seleccionadas...");
            juego.jugar(playerName); // Pasar el nombre del jugador a este método

            volverAJugar = confirmar("¿Deseas volver a jugar? (si/no): ");
        } while (volverAJugar);
    }

    private void mostrarBestScores() {
        System.out.println("=== Mejores Puntajes ===");
        juego.mostrarBestScores(); // Mostrar los mejores puntajes
    }

    private boolean confirmar(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String respuesta = scanner.nextLine().trim().toLowerCase();

            switch (respuesta) {
                case "si", "sí" -> {
                    return true;
                }
                case "no" -> {
                    return false;
                }
                default -> System.out.println("Respuesta no válida. Escribe 'si' o 'no'.");
            }
        }
    }
}
