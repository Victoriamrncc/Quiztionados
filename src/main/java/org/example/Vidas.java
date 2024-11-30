package org.example;

import java.util.Scanner;

public class Vidas extends Juego {
    private int vidas;

    public Vidas() {
        super();
        this.vidas = 3; // Inicia con 3 vidas
    }

    @Override
    public void jugar() {
        if (preguntasFiltradas == null || preguntasFiltradas.isEmpty()) {
            System.out.println("No hay preguntas cargadas.\n");
            return;
        }

        Scanner scanner = new Scanner(System.in);

        for (Pregunta pregunta : preguntasFiltradas) {
            if (vidas <= 0) {
                System.out.println("¡Se acabaron tus vidas! Fin del juego.\n");
                break;
            }

            System.out.println(pregunta.getEnunciado());

            switch (pregunta.getTipoPregunta()) {
                case "input" -> manejarPreguntaTexto(scanner, pregunta);
                case "selección múltiple", "verdadero/falso" -> manejarPreguntaOpciones(scanner, pregunta);
                default -> System.out.println("Tipo de pregunta desconocido.\n");
            }
        }

        if (vidas > 0) {
            System.out.println("¡Has completado todas las preguntas de esta categoría y dificultad!");
        }
    }

    private void manejarPreguntaTexto(Scanner scanner, Pregunta pregunta) {
        String respuesta;
        do {
            System.out.print("Escribe tu respuesta: ");
            respuesta = scanner.nextLine();
            if (!ValidadorRespuestas.validarRespuestaTexto(respuesta)) {
                System.out.println("Respuesta no válida. Por favor, ingresa un texto válido.");
            }
        } while (!ValidadorRespuestas.validarRespuestaTexto(respuesta));

        if (respuesta.equalsIgnoreCase(pregunta.getRespuestaCorrecta())) {
            System.out.println("¡Correcto!\n");
            puntaje.sumarPuntos(10);
        } else {
            System.out.println("Incorrecto. La respuesta correcta era: " + pregunta.getRespuestaCorrecta() + "\n");
            perderVida();
        }
    }

    private void manejarPreguntaOpciones(Scanner scanner, Pregunta pregunta) {
        boolean respuestaValida = false;
        while (!respuestaValida) {
            for (int i = 0; i < pregunta.getOpciones().size(); i++) {
                System.out.println((i + 1) + ". " + pregunta.getOpciones().get(i));
            }

            System.out.print("Tu respuesta (número): ");
            int respuesta = scanner.nextInt();
            scanner.nextLine();

            if (respuesta >= 1 && respuesta <= pregunta.getOpciones().size()) {
                String opcionSeleccionada = pregunta.getOpciones().get(respuesta - 1);

                if (opcionSeleccionada.equalsIgnoreCase(pregunta.getRespuestaCorrecta())) {
                    System.out.println("¡Correcto!\n");
                    puntaje.sumarPuntos(10);
                } else {
                    System.out.println("Incorrecto. La respuesta correcta era: " + pregunta.getRespuestaCorrecta() + "\n");
                    perderVida();
                }

                respuestaValida = true;
            } else {
                System.out.println("Opción no válida. Por favor, selecciona una opción dentro del rango.\n");
            }
        }
    }

    private void perderVida() {
        vidas--;
        if (vidas > 0) {
            System.out.println("Te quedan " + vidas + " vidas.\n");
        } else {
            System.out.println("¡Has perdido todas tus vidas!\n");
        }
    }
}