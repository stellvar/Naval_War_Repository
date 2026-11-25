package co.edu.uptc.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Partida {
    public static final String NOMBRE_COMPUTADOR = "Computador";

    private static final int[][] VECINAS = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    private String jugador1;
    private String jugador2;
    private boolean contraComputador;
    private Tablero tablero1;
    private Tablero tablero2;
    private String jugadorActual;
    private String ganador;
    private int[] ultimoImpacto;
    private transient Random rd;

    private Partida() {
    }

    public Partida(String jugador1) {
        this(jugador1, NOMBRE_COMPUTADOR, true);
        tablero2.colocarFlotaAleatoria();
    }

    public Partida(String jugador1, String jugador2) {
        this(jugador1, jugador2, false);
    }

    private Partida(String jugador1, String jugador2, boolean contraComputador) {
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

    private static void validarJugadores(String jugador1, String jugador2, boolean contraComputador) {
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

    private static boolean esNombreNoValido(String nombre) {
        return nombre == null || nombre.isBlank() || nombre.equalsIgnoreCase(NOMBRE_COMPUTADOR);
    }

    private static boolean mismoJugador(String a, String b) {
        return a != null && a.equalsIgnoreCase(b);
    }

    public boolean puedeComenzar() {
        return tablero1.estaListo() && tablero2.estaListo();
    }

    public Impacto disparar(int fila, int columna) {
        if (esTurnoComputador()) {
            return Impacto.INVALIDO;
        }
        return ejecutarDisparo(fila, columna);
    }

    public Impacto disparoComputador() {
        if (!esTurnoComputador() || hayGanador() || !puedeComenzar()) {
            return Impacto.INVALIDO;
        }
        int[] casilla = elegirCasillaComputador();
        Impacto resultado = ejecutarDisparo(casilla[0], casilla[1]);
        if (resultado == Impacto.TOCADO) {
            ultimoImpacto = casilla;
        } else if (resultado == Impacto.HUNDIDO) {
            ultimoImpacto = null;
        }
        return resultado;
    }

    private int[] elegirCasillaComputador() {
        if (rd == null) {
            rd = new Random();
        }
        if (ultimoImpacto != null) {
            List<int[]> cercanas = new ArrayList<>();
            for (int[] desplazamiento : VECINAS) {
                int fila = ultimoImpacto[0] + desplazamiento[0];
                int columna = ultimoImpacto[1] + desplazamiento[1];
                if (tablero1.sinDisparar(fila, columna)) {
                    cercanas.add(new int[]{fila, columna});
                }
            }
            if (!cercanas.isEmpty()) {
                return cercanas.get(rd.nextInt(cercanas.size()));
            }
        }
        return tablero1.casillaSinDisparar(rd);
    }

    private Impacto ejecutarDisparo(int fila, int columna) {
        if (hayGanador() || !puedeComenzar()) {
            return Impacto.INVALIDO;
        }
        Tablero rival = getTableroRival();
        Impacto resultado = rival.recibirDisparo(fila, columna);
        if (resultado == Impacto.INVALIDO) {
            return resultado;
        }
        if (rival.flotaHundida()) {
            ganador = jugadorActual;
        } else if (resultado == Impacto.AGUA) {
            cambiarTurno();
        }
        return resultado;
    }

    private void cambiarTurno() {
        jugadorActual = mismoJugador(jugadorActual, jugador1) ? jugador2 : jugador1;
    }

    public int getTurnos(String jugador) {
        Tablero rival;
        if (mismoJugador(jugador, jugador1)) {
            rival = tablero2;
        } else if (mismoJugador(jugador, jugador2)) {
            rival = tablero1;
        } else {
            return 0;
        }
        boolean gano = mismoJugador(ganador, jugador);
        return rival.contarDisparosAgua() + (gano ? 1 : 0);
    }

    public boolean esTurnoComputador() {
        return contraComputador && mismoJugador(jugadorActual, jugador2);
    }

    public boolean hayGanador() {
        return ganador != null;
    }

    public String getNombreGanador() {
        return ganador;
    }

    public String getNombrePerdedor() {
        if (ganador == null) {
            return null;
        }
        return mismoJugador(ganador, jugador1) ? jugador2 : jugador1;
    }

    public String getNombreTurnoActual() {
        return jugadorActual;
    }

    public Tablero getTableroRival() {
        return mismoJugador(jugadorActual, jugador1) ? tablero2 : tablero1;
    }

    public String getNombreJugador1() {
        return jugador1;
    }

    public String getNombreJugador2() {
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
