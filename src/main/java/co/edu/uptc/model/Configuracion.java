package co.edu.uptc.model;

public class Configuracion {
    private int volumenMusica;
    private int volumenEfectos;
    private boolean vozDeMando;
    private String idioma;

    public Configuracion() {
        this.volumenMusica = 50;
        this.volumenEfectos = 50;
        this.vozDeMando = false;
        this.idioma = "es";
    }

    public int getVolumenMusica() {
        return volumenMusica;
    }

    public int getVolumenEfectos() {
        return volumenEfectos;
    }

    public boolean isVozDeMando() {
        return vozDeMando;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setVolumenMusica(int volumenMusica) {
        this.volumenMusica = limitarVolumen(volumenMusica);
    }

    public void setVolumenEfectos(int volumenEfectos) {
        this.volumenEfectos = limitarVolumen(volumenEfectos);
    }

    public void setVozDeMando(boolean vozDeMando) {
        this.vozDeMando = vozDeMando;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    private int limitarVolumen(int volumen) {
        if (volumen < 0) {
            return 0;
        }
        if (volumen > 100) {
            return 100;
        }
        return volumen;
    }
}

