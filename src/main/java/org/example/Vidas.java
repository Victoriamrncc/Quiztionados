package org.example;

public class Vidas {
    private int vidas;

    public Vidas() {
        this.vidas = 3;
    }

    public int getVidas() {
        return vidas;
    }

    public void setVidas(int vidas) {
        this.vidas = vidas;
    }

    public void perderVida() {
        setVidas(getVidas() - 1);

        // Notificar cambios sin imprimir directamente
        if (getVidas() <= 0) {
            notificarCambio("¡Has perdido todas tus vidas!");
        } else {
            notificarCambio("Te quedan " + getVidas() + " vidas.");
        }
    }

    private void notificarCambio(String mensaje) {
        // Aquí se podría integrar con un sistema de listeners o eventos para la GUI
        System.out.println(mensaje); // Temporal, sustituir por integración con la GUI
    }
}