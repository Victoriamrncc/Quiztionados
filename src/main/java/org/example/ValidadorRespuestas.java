package org.example;

public class ValidadorRespuestas {
    public static boolean validarRespuestaSeleccion(int respuesta, int numeroOpciones) {
        return respuesta >= 1 && respuesta <= numeroOpciones;
    }

    public static boolean validarRespuestaTexto(String respuesta) {
        return respuesta != null && !respuesta.trim().isEmpty();
    }

    public static boolean validarRespuestaVerdaderoFalso(String respuesta) {
        return respuesta.equalsIgnoreCase("verdadero") || respuesta.equalsIgnoreCase("falso");
    }
}

