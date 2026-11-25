package co.edu.uptc.model;

import java.util.Random;

public class Tablero {
    public static final int TAMANO = 10;

    private static final int SIN_DISPARAR = 0;
    private static final int AGUA = 1;
    private static final int TOCADO = 2;

    private PortaAviones portaaviones;
    private Acorazado acorazado;
    private Crucero crucero;
    private Destructor destructor;
    private int[][] disparos;
    private transient Barco[] barcos;
    private transient Random rd;

    public Tablero() {
        this.portaaviones = new PortaAviones();
        this.acorazado = new Acorazado();
        this.crucero = new Crucero();
        this.destructor = new Destructor();
        this.disparos = new int[TAMANO][TAMANO];
        this.rd = new Random();
    }

    public Barco[] getBarcos() {
        if (barcos == null) {
            barcos = new Barco[]{portaaviones, acorazado, crucero, destructor};
        }
        return barcos;
    }

    private boolean dentroDelTablero(int fila, int columna) {
        return fila >= 0 && fila < TAMANO && columna >= 0 && columna < TAMANO;
    }

    private boolean esDeEsteTablero(Barco barco) {
        for (Barco propio : getBarcos()) {
            if (propio == barco) {
                return true;
            }
        }
        return false;
    }

    public boolean colocarBarco(Barco barco, int fila, int columna, boolean horizontal) {
        if (!esDeEsteTablero(barco)) {
            return false;
        }
        int filaFinal = fila;
        int columnaFinal = columna;
        if (horizontal) {
            columnaFinal = columna + barco.getTamano() - 1;
        } else {
            filaFinal = fila + barco.getTamano() - 1;
        }
        if (!dentroDelTablero(fila, columna) || !dentroDelTablero(filaFinal, columnaFinal)) {
            return false;
        }
        for (Barco otro : getBarcos()) {
            if (otro != barco) {
                for (int j = 0; j < barco.getTamano(); j++) {
                    int f = fila;
                    int c = columna;
                    if (horizontal) {
                        c = columna + j;
                    } else {
                        f = fila + j;
                    }
                    if (otro.ocupa(f, c)) {
                        return false;
                    }
                }
            }
        }
        barco.colocar(fila, columna, horizontal);
        return true;
    }

    public void colocarFlotaAleatoria() {
        if (rd == null) {
            rd = new Random();
        }
        for (Barco barco : getBarcos()) {
            barco.quitar();
        }
        for (Barco barco : getBarcos()) {
            boolean colocado = false;
            while (!colocado) {
                int fila = rd.nextInt(TAMANO);
                int columna = rd.nextInt(TAMANO);
                boolean horizontal = rd.nextBoolean();
                colocado = colocarBarco(barco, fila, columna, horizontal);
            }
        }
    }

    public boolean estaListo() {
        for (Barco barco : getBarcos()) {
            if (!barco.estaColocado()) {
                return false;
            }
        }
        return true;
    }

    public Impacto recibirDisparo(int fila, int columna) {
        if (!sinDisparar(fila, columna)) {
            return Impacto.INVALIDO;
        }
        for (Barco barco : getBarcos()) {
            if (barco.ocupa(fila, columna)) {
                barco.recibirImpacto(fila, columna);
                disparos[fila][columna] = TOCADO;
                return barco.estaHundido() ? Impacto.HUNDIDO : Impacto.TOCADO;
            }
        }
        disparos[fila][columna] = AGUA;
        return Impacto.AGUA;
    }

    public boolean sinDisparar(int fila, int columna) {
        return dentroDelTablero(fila, columna) && disparos[fila][columna] == SIN_DISPARAR;
    }

    public boolean esImpacto(int fila, int columna) {
        return disparos[fila][columna] == TOCADO;
    }

    public int barcosRestantes() {
        int restantes = 0;
        for (Barco barco : getBarcos()) {
            if (!barco.estaHundido()) {
                restantes++;
            }
        }
        return restantes;
    }

    public boolean flotaHundida() {
        return barcosRestantes() == 0;
    }

    public int contarDisparosAgua() {
        int total = 0;
        for (int f = 0; f < TAMANO; f++) {
            for (int c = 0; c < TAMANO; c++) {
                if (disparos[f][c] == AGUA) {
                    total++;
                }
            }
        }
        return total;
    }

    public int[] casillaSinDisparar(Random azar) {
        int disponibles = 0;
        for (int f = 0; f < TAMANO; f++) {
            for (int c = 0; c < TAMANO; c++) {
                if (disparos[f][c] == SIN_DISPARAR) {
                    disponibles++;
                }
            }
        }
        if (disponibles == 0) {
            return null;
        }
        int elegida = azar.nextInt(disponibles);
        for (int f = 0; f < TAMANO; f++) {
            for (int c = 0; c < TAMANO; c++) {
                if (disparos[f][c] == SIN_DISPARAR && elegida-- == 0) {
                    return new int[]{f, c};
                }
            }
        }
        return null;
    }
}
