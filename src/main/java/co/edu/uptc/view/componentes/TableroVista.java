package co.edu.uptc.view.componentes;

public final class TableroVista {

    public static final int N = 10;
    public static final int SIN_DISPARAR = 0;
    public static final int AGUA = 1;
    public static final int TOCADO = 2;

    /* un barco dibujado  */
    public static final class Nave {
        private final int tamano;
        private int fila;
        private int columna;
        private boolean horizontal;
        private final boolean[] impactos;

        public Nave(int tamano, int fila, int columna, boolean horizontal, int... segmentosImpactados) {
            this.tamano = tamano;
            this.fila = fila;
            this.columna = columna;
            this.horizontal = horizontal;
            this.impactos = new boolean[tamano];
            for (int s : segmentosImpactados) {
                impactos[s] = true;
            }
        }

        public int getTamano() {
            return tamano;
        }

        public int getFila() {
            return fila;
        }

        public int getColumna() {
            return columna;
        }

        public boolean isHorizontal() {
            return horizontal;
        }

        public boolean[] getImpactos() {
            return impactos;
        }

        public boolean estaHundido() {
            for (boolean impacto : impactos) {
                if (!impacto) {
                    return false;
                }
            }
            return true;
        }

        public boolean ocupa(int f, int c) {
            return horizontal
                    ? f == fila && c >= columna && c < columna + tamano
                    : c == columna && f >= fila && f < fila + tamano;
        }

        public void mover(int fila, int columna, boolean horizontal) {
            this.fila = fila;
            this.columna = columna;
            this.horizontal = horizontal;
        }
    }

    private final Nave[] naves;
    private final int[][] disparos = new int[N][N];

    private TableroVista(Nave[] naves) {
        this.naves = naves;
    }

    public Nave[] getNaves() {
        return naves;
    }

    public int getDisparo(int fila, int columna) {
        return disparos[fila][columna];
    }

    public Nave getNaveEn(int fila, int columna) {
        for (Nave nave : naves) {
            if (nave.ocupa(fila, columna)) {
                return nave;
            }
        }
        return null;
    }

    public int navesRestantes() {
        int restantes = 0;
        for (Nave nave : naves) {
            if (!nave.estaHundido()) {
                restantes++;
            }
        }
        return restantes;
    }

    /* indica si la nave cabe en la posicion dada sin salirse ni chocar con otra */
    public boolean puedeColocar(Nave nave, int fila, int columna, boolean horizontal) {
        int filaFinal = horizontal ? fila : fila + nave.getTamano() - 1;
        int columnaFinal = horizontal ? columna + nave.getTamano() - 1 : columna;
        if (fila < 0 || columna < 0 || filaFinal >= N || columnaFinal >= N) {
            return false;
        }
        for (int i = 0; i < nave.getTamano(); i++) {
            int f = horizontal ? fila : fila + i;
            int c = horizontal ? columna + i : columna;
            for (Nave otra : naves) {
                if (otra != nave && otra.ocupa(f, c)) {
                    return false;
                }
            }
        }
        return true;
    }

    /* mueve la nave a la posicion dada si es valida */
    public boolean colocar(Nave nave, int fila, int columna, boolean horizontal) {
        if (!puedeColocar(nave, fila, columna, horizontal)) {
            return false;
        }
        nave.mover(fila, columna, horizontal);
        return true;
    }

    /* gira la nave sobre su primera casilla si hay espacio */
    public boolean rotar(Nave nave) {
        return colocar(nave, nave.getFila(), nave.getColumna(), !nave.isHorizontal());
    }

    private TableroVista disparar(int[]... casillas) {
        for (int[] casilla : casillas) {
            Nave nave = getNaveEn(casilla[0], casilla[1]);
            disparos[casilla[0]][casilla[1]] = nave == null ? AGUA : TOCADO;
        }
        return this;
    }

    /* tablero ejemplo aliado */
    public static TableroVista demoPropio() {
        return new TableroVista(new Nave[]{
                new Nave(5, 1, 1, true, 3),
                new Nave(4, 3, 7, false, 0, 1),
                new Nave(3, 7, 2, true),
                new Nave(2, 5, 4, true)
        }).disparar(new int[][]{{1, 4}, {3, 7}, {4, 7}, {0, 0}, {6, 6}, {8, 8}, {9, 2}});
    }

    /*tablero ejemplo enemigo */
    public static TableroVista demoEnemigo() {
        return new TableroVista(new Nave[]{
                new Nave(5, 1, 1, true, 0, 1, 2, 3, 4),
                new Nave(4, 3, 7, false, 0),
                new Nave(3, 7, 2, true),
                new Nave(2, 5, 4, true)
        }).disparar(new int[][]{{1, 1}, {1, 2}, {1, 3}, {1, 4}, {1, 5}, {3, 7}, {0, 9}, {9, 0}, {6, 6}, {8, 4}});
    }
}
