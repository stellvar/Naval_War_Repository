package co.edu.uptc.model;

public class Barco {
    private String nombre;
    private int tamano;
    private int vida;
    private int fila;
    private int columna;
    private boolean horizontal;
    private boolean[] impactos;

    public Barco(String nombre, int tamano) {
        this.nombre = nombre;
        this.tamano = tamano;
        this.vida = tamano;
        this.fila = -1;
        this.columna = -1;
        this.horizontal = true;
        this.impactos = new boolean[tamano];
    }

    public boolean estaColocado() {
        return fila != -1;
    }

    public void colocar(int fila, int columna, boolean horizontal) {
        this.fila = fila;
        this.columna = columna;
        this.horizontal = horizontal;
    }

    public void quitar() {
        this.fila = -1;
        this.columna = -1;
    }

    public int posicionEn(int fila, int columna) {
        if (!estaColocado()) {
            return -1;
        }
        if (horizontal) {
            if (fila == this.fila && columna >= this.columna && columna < this.columna + tamano) {
                return columna - this.columna;
            }
        } else {
            if (columna == this.columna && fila >= this.fila && fila < this.fila + tamano) {
                return fila - this.fila;
            }
        }
        return -1;
    }

    public boolean ocupa(int fila, int columna) {
        return posicionEn(fila, columna) != -1;
    }

    public boolean recibirImpacto(int fila, int columna) {
        int posicion = posicionEn(fila, columna);
        if (posicion == -1 || impactos[posicion]) {
            return false;
        }
        impactos[posicion] = true;
        vida--;
        return true;
    }

    public boolean estaHundido() {
        return vida == 0;
    }

    public String getNombre() {
        return nombre;
    }

    public int getTamano() {
        return tamano;
    }

    public int getVida() {
        return vida;
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
}
