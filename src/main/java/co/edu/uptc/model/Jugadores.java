package co.edu.uptc.model;

import java.util.ArrayList;
import java.util.Comparator;

public class Jugadores {

    private ArrayList<Jugador> listaJugadores;

    public Jugadores() {
        this.listaJugadores = new ArrayList<>();
    }

    public ArrayList<Jugador> getListaJugadores() {
        return listaJugadores;
    }

    public void agregarJugador(Jugador jugador) {
        listaJugadores.add(jugador);
    }

    public Jugador buscarJugador(String nombre) {
        if (nombre == null) {
            return null;
        }
        for (Jugador jugador : listaJugadores) {
            if (jugador.getNombre().equalsIgnoreCase(nombre)) {
                return jugador;
            }
        }
        return null;
    }


    public Jugador guardarJugador(String nombre, String biografia, int avatar) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return null;
        }
        String limpio = nombre.trim();
        if (limpio.equalsIgnoreCase(Partida.nombreComputador)) {
            return null;
        }
        Jugador jugador = buscarJugador(limpio);
        if (jugador == null) {
            jugador = new Jugador(limpio, biografia, avatar);
            agregarJugador(jugador);
        } else {
            jugador.setBiografia(biografia);
            jugador.setAvatar(avatar);
        }
        return jugador;
    }

    public ArrayList<Jugador> getRanking() {
        ArrayList<Jugador> ranking = new ArrayList<>();
        for (Jugador jugador : listaJugadores) {
            if (jugador.getMejorTurnos() > 0) {
                ranking.add(jugador);
            }
        }
        ranking.sort(Comparator.comparingInt(Jugador::getMejorTurnos));
        return ranking;
    }
}
