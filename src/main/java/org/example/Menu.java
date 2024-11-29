package org.example;

import java.util.Scanner;

public class Menu {
    private Juego juego;

    public Menu(Juego juego) {
        this.juego = juego;
    }

    public void mostrarMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean continuar = true;

        while (continuar) {
            System.out.println("=== MENÚ DEL JUEGO ===");
            System.out.println("1. Iniciar Juego");
            System.out.println("2. Salir");
            System.out.print("Selecciona una opción: ");

            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    juego.seleccionarCategoria(); // Solicita al usuario seleccionar una categoría
                    juego.seleccionarDificultad(); // Luego solicita seleccionar la dificultad
                    juego.jugar(); // Inicia el juego

                    // Preguntar al usuario si desea volver a jugar
                    while (true) {
                        System.out.print("¿Deseas volver a jugar? (si/no): ");
                        String decision = scanner.next().toLowerCase();

                        if (decision.equals("si")) {
                            // Si el usuario quiere volver a jugar, se reinicia el juego
                            juego.seleccionarCategoria(); // Solicita al usuario seleccionar una categoría
                            juego.seleccionarDificultad(); // Luego solicita seleccionar la dificultad
                            juego.jugar(); // Inicia el juego nuevamente
                            break;
                        } else if (decision.equals("no")) {
                            // Si el usuario no quiere volver a jugar, se sale
                            System.out.println("¡Gracias por jugar!");
                            continuar = false;
                            break;
                        } else {
                            // Respuesta no válida, pedimos nuevamente
                            System.out.println("Respuesta no válida. Por favor, escribe 'si' o 'no'.");
                        }
                    }
                    break;
                case 2:
                    System.out.println("¡Gracias por jugar!");
                    continuar = false;
                    break;
                default:
                    System.out.println("Opción no válida. Intenta de nuevo.");
            }
        }
    }
}