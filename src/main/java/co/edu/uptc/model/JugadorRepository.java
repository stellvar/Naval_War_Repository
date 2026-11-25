package co.edu.uptc.model;

import co.edu.uptc.persistencia.JsonRepository;

import java.io.File;
import java.util.ArrayList;

public class JugadorRepository {

    private static final String ARCHIVO = "jugadores.json";

    private final JsonRepository<Jugadores> repository;
    private Jugadores misJugadores;

    public JugadorRepository() {
        this(JsonRepository.CARPETA_DATOS);
    }

    public JugadorRepository(String carpeta) {
        this.repository = new JsonRepository<>(new File(carpeta, ARCHIVO).getPath(), Jugadores.class);
        Jugadores cargados = repository.readFile();
        this.misJugadores = cargados != null ? cargados : new Jugadores();
    }

    public Jugador guardarJugador(String nombre, String biografia, int avatar) {
        Jugador jugador = misJugadores.guardarJugador(nombre, biografia, avatar);
        if (jugador != null) {
            persistir();
        }
        return jugador;
    }

    public Jugador buscarJugador(String nombre) {
        return misJugadores.buscarJugador(nombre);
    }

    private boolean registrarVictoria(String nombre, int turnos) {
        Jugador jugador = misJugadores.buscarJugador(nombre);
        if (jugador == null) {
            return false;
        }
        jugador.registrarVictoria(turnos);
        persistir();
        return true;
    }

    private boolean registrarDerrota(String nombre) {
        Jugador jugador = misJugadores.buscarJugador(nombre);
        if (jugador == null) {
            return false;
        }
        jugador.registrarDerrota();
        persistir();
        return true;
    }

    public ArrayList<Jugador> getRanking() {
        return misJugadores.getRanking();
    }

    public boolean registrarResultadoPartida(Partida partida) {
        if (!partida.hayGanador()) {
            return false;
        }
        String ganador = partida.getNombreGanador();
        String perdedor = partida.getNombrePerdedor();
        int turnos = partida.getTurnos(ganador);
        boolean victoriaOk = esComputador(ganador) || registrarVictoria(ganador, turnos);
        boolean derrotaOk = esComputador(perdedor) || registrarDerrota(perdedor);
        return victoriaOk && derrotaOk;
    }

    private static boolean esComputador(String nombre) {
        return nombre.equalsIgnoreCase(Partida.NOMBRE_COMPUTADOR);
    }

    private void persistir() {
        repository.writeFile(misJugadores);
    }
}
