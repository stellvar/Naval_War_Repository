package co.edu.uptc.model;

import java.util.ArrayList;

public class Jugador {
    public static final int cantidadAvatares = 6;
    public static final int avatarPorDefecto = 5;

    private static final int DERROTA = 0;

    private String nombre;
    private String biografia;
    private int avatar;

    private ArrayList<Integer> resultados = new ArrayList<>();

    private Jugador() {
    }

    public Jugador(String nombre, String biografia, int avatar) {
        this.nombre = nombre;
        this.biografia = biografia == null ? "" : biografia;
        this.avatar = avatarValido(avatar) ? avatar : avatarPorDefecto;
    }

    private static boolean avatarValido(int avatar) {
        return avatar >= 0 && avatar < cantidadAvatares;
    }

    public void registrarVictoria(int turnos) {
        resultados.add(Math.max(1, turnos));
    }

    public void registrarDerrota() {
        resultados.add(DERROTA);
    }

    public String getNombre() {
        return nombre;
    }

    public String getBiografia() {
        return biografia;
    }

    public int getAvatar() {
        return avatar;
    }

    public int getPartidasJugadas() {
        return resultados.size();
    }

    public int getPartidasGanadas() {
        int ganadas = 0;
        for (int resultado : resultados) {
            if (resultado != DERROTA) {
                ganadas++;
            }
        }
        return ganadas;
    }

    public int getPartidasPerdidas() {
        int perdidas = 0;
        for (int resultado : resultados) {
            if (resultado == DERROTA) {
                perdidas++;
            }
        }
        return perdidas;
    }

    public int getMejorTurnos() {
        int mejor = 0;
        for (int resultado : resultados) {
            if (resultado != DERROTA && (mejor == 0 || resultado < mejor)) {
                mejor = resultado;
            }
        }
        return mejor;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia == null ? "" : biografia;
    }

    public void setAvatar(int avatar) {
        if (avatarValido(avatar)) {
            this.avatar = avatar;
        }
    }
}
