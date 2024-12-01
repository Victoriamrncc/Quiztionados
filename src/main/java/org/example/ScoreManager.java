package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ScoreManager {
    private Map<String, Integer> bestScores;
    private static final String SCORE_FILE = "src/main/resources/scores.json";

    public ScoreManager() {
        bestScores = new HashMap<>();
        cargarBestScores();
    }

    private void cargarBestScores() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            File file = new File(SCORE_FILE);
            if (file.exists()) {
                bestScores = mapper.readValue(file, new TypeReference<Map<String, Integer>>() {});
            }
        } catch (IOException e) {
            System.out.println("No se pudieron cargar los mejores puntajes: " + e.getMessage());
        }
    }

    public void guardarBestScores() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File(SCORE_FILE), bestScores);
        } catch (IOException e) {
            System.out.println("No se pudieron guardar los mejores puntajes: " + e.getMessage());
        }
    }

    public void actualizarBestScore(String playerName, int score) {
        int maxScore = Math.max(bestScores.getOrDefault(playerName, 0), score);
        bestScores.put(playerName, maxScore);
        guardarBestScores();
    }

    public void mostrarBestScores() {
        System.out.println("=== Mejores puntajes ===");
        if (bestScores.isEmpty()) {
            System.out.println("No hay puntajes registrados.");
        } else {
            bestScores.forEach((name, score) -> System.out.println(name + ": " + score));
        }
    }

    public Map<String, Integer> getBestScores() {
        return bestScores;
    }
}
