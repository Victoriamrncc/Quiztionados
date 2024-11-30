package org.example;

import java.util.List;

public class ValidadorRespuestas {
    public static boolean validarRespuestaSeleccion(int respuesta, int numeroOpciones) {
        return respuesta >= 1 && respuesta <= numeroOpciones;
    }

    public static boolean validarRespuestaTexto(String respuesta) {
        return respuesta != null && !respuesta.trim().isEmpty();
    }

    public static int validarEntradaMenu(String entrada, int opcionesValidas) {
        try {
            int opcion = Integer.parseInt(entrada);
            if (opcion >= 1 && opcion <= opcionesValidas) {
                return opcion;
            }
        } catch (NumberFormatException e) {

        }
        return -1;
    }

}

