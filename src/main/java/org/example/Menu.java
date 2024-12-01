package org.example;

import java.util.Scanner;
import java.util.stream.Collectors;

public class Menu {
    private Juego juego;
    private final Scanner scanner;

    public Menu(Juego juego) {
        this.juego = juego;
        this.scanner = new Scanner(System.in);
    }

//    public void mostrarMenu() {
//        boolean continuar = true;
//
//        while (continuar) {
//            System.out.println("=== MENÚ DEL JUEGO ===");
//            System.out.println("1. Iniciar Juego");
//            System.out.println("2. Ver Mejores Puntajes");
//            System.out.println("3. Salir");
//            System.out.print("Selecciona una opción: ");
//
//            int opcion = scanner.nextInt();
//            scanner.nextLine();
//
//            switch (opcion) {
//                case 1 -> iniciarJuego();
//                case 2 -> mostrarBestScores();
//                case 3 -> {
//                    System.out.println("¡Gracias por jugar!");
//                    continuar = false;
//                }
//                default -> System.out.println("Opción no válida.");
//            }
//        }
//    }

//    public void iniciarJuego() {
//        boolean volverAJugar;
//        do {
//            System.out.println("Por favor, ingresa tu nombre: ");
//            String playerName = scanner.nextLine(); // Obtener el nombre del jugador
//
//            // Pedir al usuario que seleccione una categoría
//            System.out.println("Selecciona una categoría (ejemplo: Historia, Ciencia, Geografía): ");
//            String categoria = scanner.nextLine().trim();
//
//            // Pasar la categoría seleccionada al método
//            seleccionarCategoria(categoria);
//
//            // Continuar con la selección de dificultad
//            seleccionarDificultad();
//
//            System.out.println("Iniciando el juego con preguntas seleccionadas...");
//            juego.jugar(playerName); // Pasar el nombre del jugador a este método
//
//            volverAJugar = confirmar("¿Deseas volver a jugar? (si/no): ");
//        } while (volverAJugar);
//    }


    public void seleccionarCategoria(String categoria) {
        if (categoria == null || categoria.isEmpty()) {
            System.out.println("No se seleccionó ninguna categoría.");
            return;
        }

        System.out.println("Categoría seleccionada: " + categoria);

        juego.preguntasFiltradas = juego.preguntas.stream()
                .filter(pregunta -> pregunta.getCategoria() != null &&
                        pregunta.getCategoria().trim().equalsIgnoreCase(categoria))
                .collect(Collectors.toList());

        if (juego.preguntasFiltradas.isEmpty()) {
            System.out.println("No hay preguntas disponibles para esta categoría.\n");
        } else {
            System.out.println("Preguntas disponibles: " + juego.preguntasFiltradas.size());
        }
    }


    private void seleccionarDificultad() {
        String dificultad = null;

        while (dificultad == null) {
            System.out.println("Selecciona la dificultad del juego:");
            System.out.println("1. Fácil");
            System.out.println("2. Intermedio");
            System.out.println("3. Difícil");
            System.out.print("Selecciona una opción: ");
            int dificultadSeleccionada = scanner.nextInt();
            scanner.nextLine();

            dificultad = switch (dificultadSeleccionada) {
                case 1 -> "fácil";
                case 2 -> "intermedio";
                case 3 -> "difícil";
                default -> {
                    System.out.println("Opción no válida. Por favor, selecciona una opción válida.\n");
                    yield null;
                }
            };
        }

        String finalDificultad = dificultad;

        juego.preguntasFiltradas = juego.preguntasFiltradas.stream()
                .filter(pregunta -> pregunta.getDificultad() != null && pregunta.getDificultad().equalsIgnoreCase(finalDificultad))
                .collect(Collectors.toList());

        if (juego.preguntasFiltradas.isEmpty()) {
            System.out.println("No hay preguntas disponibles para esta dificultad.\n");
        } else {
            System.out.println("Dificultad seleccionada: " + dificultad);
        }
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
