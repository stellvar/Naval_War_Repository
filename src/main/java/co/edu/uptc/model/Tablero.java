package co.edu.uptc.model;
import java.util.Random;

public class Tablero {
    public static int tamano = 10;
    public static int sinDisparar = 0;
    public static int agua = 1;
    public  int tocado = 2;
    public  int hundido = 3;
    public static int invalido = -1;

    private PortaAviones portaaviones;
    private Acorazado acorazado;
    private Crucero crucero;
    private Destructor destructor;
    private int[][] disparos;
    private transient Random rd;

    public Tablero() {
        this.portaaviones = new PortaAviones();
        this.acorazado = new Acorazado();
        this.crucero = new Crucero();
        this.destructor = new Destructor();
        this.disparos = new int[tamano][tamano];
        this.rd = new Random();
    }

    public Barco[] getBarcos() {
        Barco[] barcos = {portaaviones, acorazado, crucero, destructor};
        return barcos;
    }

    public boolean dentroDelTablero(int fila, int columna) {
        return fila >= 0 && fila < tamano && columna >= 0 && columna < tamano;
    }

    public boolean colocarBarco(Barco barco, int fila, int columna, boolean horizontal) {
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

        Barco[] barcos = getBarcos();
        for (int i = 0; i < barcos.length; i++) {
            if (barcos[i] != barco) {
                for (int j = 0; j < barco.getTamano(); j++) {
                    int f = fila;
                    int c = columna;
                    if (horizontal) {
                        c = columna + j;
                    } else {
                        f = fila + j;
                    }
                    if (barcos[i].ocupa(f, c)) {
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
        Barco[] barcos = getBarcos();
        for (int i = 0; i < barcos.length; i++) {
            barcos[i].quitar();
        }
        for (int i = 0; i < barcos.length; i++) {
            boolean colocado = false;
            while (!colocado) {
                int fila = rd.nextInt(tamano);
                int columna = rd.nextInt(tamano);
                boolean horizontal = rd.nextBoolean();
                colocado = colocarBarco(barcos[i], fila, columna, horizontal);
            }
        }
    }

    public boolean estaListo() {
        Barco[] barcos = getBarcos();
        for (int i = 0; i < barcos.length; i++) {
            if (!barcos[i].estaColocado()) {
                return false;
            }
        }
        return true;
    }

    public int recibirDisparo(int fila, int columna) {
        if (!dentroDelTablero(fila, columna) || disparos[fila][columna] != sinDisparar) {
            return invalido;
        }
        Barco[] barcos = getBarcos();
        for (int i = 0; i < barcos.length; i++) {
            if (barcos[i].ocupa(fila, columna)) {
                barcos[i].recibirImpacto(fila, columna);
                disparos[fila][columna] = tocado;
                if (barcos[i].estaHundido()) {
                    return hundido;
                }
                return tocado;
            }
        }
        disparos[fila][columna] = agua;
        return agua;
    }

    public int getDisparo(int fila, int columna) {
        return disparos[fila][columna];
    }

    public Barco getBarcoEn(int fila, int columna) {
        Barco[] barcos = getBarcos();
        for (int i = 0; i < barcos.length; i++) {
            if (barcos[i].ocupa(fila, columna)) {
                return barcos[i];
            }
        }
        return null;
    }

    public int barcosRestantes() {
        int restantes = 0;
        Barco[] barcos = getBarcos();
        for (int i = 0; i < barcos.length; i++) {
            if (!barcos[i].estaHundido()) {
                restantes++;
            }
        }
        return restantes;
    }

    public boolean flotaHundida() {
        return barcosRestantes() == 0;
    }
}

