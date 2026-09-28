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

    public void colocarFlotaAleatoria() {
        /*barco en coordenadas de random numer*/
    }

    public boolean dentroDelTablero(int fila, int columna) {
        return fila >= 0 && fila < tamano && columna >= 0 && columna < tamano;
    }

    public boolean estaListo() {
        /*revisar q todos los barcos esten colocados*/
    }

    public int getDisparo(int fila, int columna) {
        return disparos[fila][columna];
    }


}

