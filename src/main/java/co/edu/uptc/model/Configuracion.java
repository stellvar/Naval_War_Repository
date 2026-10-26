package co.edu.uptc.model;

public class Configuracion {

    private static final String[] IDIOMAS_SOPORTADOS = {"es", "en"};

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

    public boolean setIdioma(String idioma) {
        if (idioma == null) {
            return false;
        }
        String normalizado = idioma.trim().toLowerCase();
        for (String soportado : IDIOMAS_SOPORTADOS) {
            if (soportado.equals(normalizado)) {
                this.idioma = normalizado;
                return true;
            }
        }
        return false;
    }

    private static int limitarVolumen(int volumen) {
        return Math.max(0, Math.min(100, volumen));
    }
}
