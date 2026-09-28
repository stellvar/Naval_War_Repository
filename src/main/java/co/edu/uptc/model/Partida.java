package co.edu.uptc.model;
import java.util.Random;

public class Partida {
    public static final String nombreComputador = "Computador";

    private String jugador1;
    private String jugador2;
    private boolean contraComputador;
    private Tablero tablero1;
    private Tablero tablero2;
    private int turnoActual;
    private int turnos1;
    private int turnos2;
    private int ganador;
    private transient Random rd;

    public Partida(String jugador1) {
        this.jugador1 = jugador1;
        this.jugador2 = nombreComputador;
        this.contraComputador = true;
        this.tablero1 = new Tablero();
        this.tablero2 = new Tablero();
        this.tablero2.colocarFlotaAleatoria();
        this.turnoActual = 1;
        this.turnos1 = 1;
        this.turnos2 = 0;
        this.ganador = 0;
        this.rd = new Random();
    }

    public Partida(String jugador1, String jugador2) {
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.contraComputador = false;
        this.tablero1 = new Tablero();
        this.tablero2 = new Tablero();
        this.turnoActual = 1;
        this.turnos1 = 1;
        this.turnos2 = 0;
        this.ganador = 0;
        this.rd = new Random();
    }

    public boolean puedeComenzar() {

    }

    private void cambiarTurno() {
        if (turnoActual == 1) {
            turnoActual = 2;
            turnos2++;
        } else {
            turnoActual = 1;
            turnos1++;
        }
    }

    public boolean hayGanador() {
        return ganador != 0;
    }

    public String getNombreGanador() {
        if (ganador == 1) {
            return jugador1;
        }
        if (ganador == 2) {
            return jugador2;
        }
        return null;
    }

    public String getNombreTurnoActual() {
        if (turnoActual == 1) {
            return jugador1;
        }
        return jugador2;
    }

    public Tablero getTableroActual() {
        if (turnoActual == 1) {
            return tablero1;
        }
        return tablero2;
    }

    public Tablero getTableroRival() {
        if (turnoActual == 1) {
            return tablero2;
        }
        return tablero1;
    }

    public String getJugador1() {
        return jugador1;
    }

    public String getJugador2() {
        return jugador2;
    }

    public boolean isContraComputador() {
        return contraComputador;
    }

    public Tablero getTablero1() {
        return tablero1;
    }

    public Tablero getTablero2() {
        return tablero2;
    }

    public int getTurnoActual() {
        return turnoActual;
    }

    public int getTurnos1() {
        return turnos1;
    }

    public int getTurnos2() {
        return turnos2;
    }

    public int getGanador() {
        return ganador;
    }
}

