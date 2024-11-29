package org.example;
import java.io.Serializable;
import java.util.List;

public class Pregunta implements Serializable {
    private String enunciado;
    private List<String> opciones;
    private String respuestaCorrecta;
    private String categoria;
    private String dificultad;

    public String getEnunciado() {
        return enunciado;
    }

    public List<String> getOpciones() {
        return opciones;
    }

    public String getRespuestaCorrecta() {
        return respuestaCorrecta;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(enunciado + "\n");
        for (int i = 0; i < opciones.size(); i++) {
            sb.append((i + 1) + ". " + opciones.get(i) + "\n");
        }
        return sb.toString();
    }
}
