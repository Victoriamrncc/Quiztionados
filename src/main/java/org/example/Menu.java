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
                    juego.seleccionarCategoria();
                    juego.seleccionarDificultad();
                    juego.jugar();

                    while (true) {
                        System.out.print("¿Deseas volver a jugar? (si/no): ");
                        String decision = scanner.next().toLowerCase();

                        if (decision.equals("si")) {
                            juego.seleccionarCategoria();
                            juego.seleccionarDificultad();
                            juego.jugar();
                            break;
                        } else if (decision.equals("no")) {
                            System.out.println("¡Gracias por jugar!");
                            continuar = false;
                            break;
                        } else {
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