package org.example;
import java.io.Serializable;
import java.util.List;

public class Pregunta implements Serializable {
    private String enunciado;
    private List<String> opciones;
    private String respuestaCorrecta;
    private String categoria;
    private String dificultad; // "facil", "intermedio", "dificil"
    private String tipoPregunta; // "multiple", "verdadero_falso", "texto"

    public String getEnunciado() {
        return enunciado;
    }

    public List<String> getOpciones() {
        return opciones;
    }

    public String getRespuestaCorrecta() {
        return respuestaCorrecta;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getDificultad() {
        return dificultad;
    }

    public String getTipoPregunta() {
        return tipoPregunta;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(enunciado + "\n");

        if (tipoPregunta.equals("selección múltiple")) {
            for (int i = 0; i < opciones.size(); i++) {
                sb.append((i + 1) + ". " + opciones.get(i) + "\n");
            }
        }
        else if (tipoPregunta.equals("verdadero/falso")) {
            sb.append("1. Verdadero\n");
            sb.append("2. Falso\n");
        }
        else if (tipoPregunta.equals("input")) {
            sb.append("Escribe tu respuesta:\n");
        }
        return sb.toString();
    }
}