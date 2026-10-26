package co.edu.uptc.model;

import java.util.Random;

public class Partida {
    public static final String nombreComputador = "Computador";

    public enum Etapa { COLOCANDO_BARCOS, EN_CURSO, TERMINADA }

    private Jugador jugador1;
    private Jugador jugador2;
    private boolean contraComputador;
    private Tablero tablero1;
    private Tablero tablero2;
    private Jugador jugadorActual;
    private Jugador ganador;
    private transient Random rd;

    private Partida() {
    }

    public Partida(Jugador jugador1) {
        this(jugador1, new Jugador(nombreComputador, "", Jugador.avatarPorDefecto), true);
        tablero2.colocarFlotaAleatoria();
    }

    public Partida(Jugador jugador1, Jugador jugador2) {
        this(jugador1, jugador2, false);
    }

    private Partida(Jugador jugador1, Jugador jugador2, boolean contraComputador) {
        validarJugadores(jugador1, jugador2, contraComputador);
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.contraComputador = contraComputador;
        this.tablero1 = new Tablero();
        this.tablero2 = new Tablero();
        this.jugadorActual = jugador1;
        this.ganador = null;
        this.rd = new Random();
    }

    private static void validarJugadores(Jugador jugador1, Jugador jugador2, boolean contraComputador) {
        if (esNombreNoValido(jugador1)) {
            throw new IllegalArgumentException("Nombre del jugador 1 no válido.");
        }
        if (contraComputador) {
            return;
        }
        if (esNombreNoValido(jugador2)) {
            throw new IllegalArgumentException("Nombre del jugador 2 no válido.");
        }
        if (mismoJugador(jugador1, jugador2)) {
            throw new IllegalArgumentException("Los jugadores deben tener nombres distintos.");
        }
    }

    private static boolean esNombreNoValido(Jugador jugador) {
        return jugador == null
                || jugador.getNombre() == null
                || jugador.getNombre().isBlank()
                || jugador.getNombre().equalsIgnoreCase(nombreComputador);
    }

    private static boolean mismoJugador(Jugador a, Jugador b) {
        return a != null && b != null && a.getNombre().equalsIgnoreCase(b.getNombre());
    }

    public boolean puedeComenzar() {
        return tablero1.estaListo() && tablero2.estaListo();
    }

    public int disparar(int fila, int columna) {
        if (esTurnoComputador()) {
            return Tablero.invalido;
        }
        return ejecutarDisparo(fila, columna);
    }

    public int disparoComputador() {
        if (!esTurnoComputador() || hayGanador() || !puedeComenzar()) {
            return Tablero.invalido;
        }
        if (rd == null) {
            rd = new Random();
        }
        int fila = rd.nextInt(Tablero.tamano);
        int columna = rd.nextInt(Tablero.tamano);
        while (tablero1.getDisparo(fila, columna) != Tablero.sinDisparar) {
            fila = rd.nextInt(Tablero.tamano);
            columna = rd.nextInt(Tablero.tamano);
        }
        return ejecutarDisparo(fila, columna);
    }

    private int ejecutarDisparo(int fila, int columna) {
        if (hayGanador() || !puedeComenzar()) {
            return Tablero.invalido;
        }
        Tablero rival = getTableroRival();
        int resultado = rival.recibirDisparo(fila, columna);
        if (resultado == Tablero.invalido) {
            return resultado;
        }
        if (rival.flotaHundida()) {
            ganador = jugadorActual;
        } else if (resultado == Tablero.agua) {
            cambiarTurno();
        }
        return resultado;
    }

    private void cambiarTurno() {
        jugadorActual = mismoJugador(jugadorActual, jugador1) ? jugador2 : jugador1;
    }

    public int getTurnos(Jugador jugador) {
        Tablero rival;
        if (mismoJugador(jugador, jugador1)) {
            rival = tablero2;
        } else if (mismoJugador(jugador, jugador2)) {
            rival = tablero1;
        } else {
            return 0;
        }
        boolean gano = ganador != null && mismoJugador(ganador, jugador);
        return rival.contarDisparosAgua() + (gano ? 1 : 0);
    }

    public boolean esTurnoComputador() {
        return contraComputador && mismoJugador(jugadorActual, jugador2);
    }

    public boolean hayGanador() {
        return ganador != null;
    }

    public Jugador getGanador() {
        return ganador;
    }

    public String getNombreGanador() {
        return ganador == null ? null : ganador.getNombre();
    }

    public Jugador getJugadorActual() {
        return jugadorActual;
    }

    public String getNombreTurnoActual() {
        return jugadorActual.getNombre();
    }

    public Tablero getTableroActual() {
        return mismoJugador(jugadorActual, jugador1) ? tablero1 : tablero2;
    }

    public Tablero getTableroRival() {
        return mismoJugador(jugadorActual, jugador1) ? tablero2 : tablero1;
    }

    public Etapa getEtapa() {
        if (hayGanador()) {
            return Etapa.TERMINADA;
        }
        if (!puedeComenzar()) {
            return Etapa.COLOCANDO_BARCOS;
        }
        return Etapa.EN_CURSO;
    }

    public Jugador getPerdedor() {
        if (ganador == null) {
            return null;
        }
        return mismoJugador(ganador, jugador1) ? jugador2 : jugador1;
    }

    public Jugador getJugador1() {
        return jugador1;
    }

    public Jugador getJugador2() {
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
}
