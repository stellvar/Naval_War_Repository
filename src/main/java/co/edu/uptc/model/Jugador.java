package co.edu.uptc.model;

public class Jugador {
    public int avatarPorDefecto = 5;

    private String nombre;
    private String biografia;
    private int avatar;
    private int partidasGanadas;
    private int partidasPerdidas;
    private int mejorTurnos;

    public Jugador(String nombre, String biografia, int avatar) {
        this.nombre = nombre;
        this.biografia = "";
        this.avatar = avatarPorDefecto;
        this.partidasGanadas = 0;
        this.partidasPerdidas = 0;
        this.mejorTurnos = 0;
        if (biografia != null) {
            this.biografia = biografia;
        }
    }

    public void registrarDerrota() {
        partidasPerdidas++;
    }

    public String getNombre() {
        return nombre;
    }

    public String getBiografia() {
        return biografia;
    }

    public int getAvatar() {
        return avatar;
    }

    public int getPartidasGanadas() {
        return partidasGanadas;
    }

    public int getPartidasPerdidas() {
        return partidasPerdidas;
    }

    public int getMejorTurnos() {
        return mejorTurnos;
    }

    public void setBiografia(String biografia) {
        if (biografia == null) {
            this.biografia = "";
        } else {
            this.biografia = biografia;
        }
    }

    public void setAvatar(int avatar) {
            this.avatar = avatar;

    }



    public void registrarVictoria(int turnos) {
        partidasGanadas++;
        if (mejorTurnos == 0 || turnos < mejorTurnos) {
            mejorTurnos = turnos;
        }
    }
}