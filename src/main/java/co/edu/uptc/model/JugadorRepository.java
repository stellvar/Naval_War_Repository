package co.edu.uptc.model;
import co.edu.uptc.persistance.JsonRepository;

import java.util.ArrayList;

public class JugadorRepository {
    private JsonRepository<Jugadores> repository;
    private Jugadores misJugadores;

    public JugadorRepository() {
        this.repository = new JsonRepository<>("src/data/jugadores.json", Jugadores.class);
    }

    public void init() {
        this.misJugadores = repository.readFile();
        if (this.misJugadores == null) {
            this.misJugadores = new Jugadores();
        }
    }

    public Jugador guardarJugador(String nombre, String biografia, int avatar) {
        if (nombre == null || nombre.trim().equals("")) {
            return null;
        }
        nombre = nombre.trim();
        Jugador jugador = misJugadores.buscarJugador(nombre);
        if (jugador == null) {
            jugador = new Jugador(nombre, biografia, avatar);
            misJugadores.agregarJugador(jugador);
        } else {
            jugador.setBiografia(biografia);
            jugador.setAvatar(avatar);
        }
        repository.writeFile(misJugadores);
        return jugador;
    }

    public Jugador buscarJugador(String nombre) {
        return misJugadores.buscarJugador(nombre);
    }

    public void registrarVictoria(String nombre, int turnos) {
        Jugador jugador = misJugadores.buscarJugador(nombre);
        if (jugador != null) {
            jugador.registrarVictoria(turnos);
            repository.writeFile(misJugadores);
        }
    }

    public void registrarDerrota(String nombre) {
        Jugador jugador = misJugadores.buscarJugador(nombre);
        if (jugador != null) {
            jugador.registrarDerrota();
            repository.writeFile(misJugadores);
        }
    }

    public ArrayList<Jugador> getListaJugadores() {
        return misJugadores.getListaJugadores();
    }

    public ArrayList<Jugador> getRanking() {
        ArrayList<Jugador> ranking = new ArrayList<>();
        for (Jugador jugador : misJugadores.getListaJugadores()) {
            if (jugador.getMejorTurnos() > 0) {
                ranking.add(jugador);
            }
        }
        for (int i = 0; i < ranking.size() - 1; i++) {
            for (int j = 0; j < ranking.size() - 1 - i; j++) {
                if (ranking.get(j).getMejorTurnos() > ranking.get(j + 1).getMejorTurnos()) {
                    Jugador aux = ranking.get(j);
                    ranking.set(j, ranking.get(j + 1));
                    ranking.set(j + 1, aux);
                }
            }
        }
        return ranking;
    }
}
