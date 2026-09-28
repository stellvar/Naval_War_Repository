package co.edu.uptc.model;
import java.util.ArrayList;

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
        for (int i = 0; i < listaJugadores.size(); i++) {
            if (listaJugadores.get(i).getNombre().equalsIgnoreCase(nombre)) {
                return listaJugadores.get(i);
            }
        }
        return null;
    }
}
