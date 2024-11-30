package org.example;

public class Vidas{
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

        if (getVidas() > 0) {
            System.out.println("Te quedan " + getVidas() + " vidas.\n");
        } else {
            System.out.println("¡Has perdido todas tus vidas!\n");
        }
    }
}
