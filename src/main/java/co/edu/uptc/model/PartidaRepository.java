package co.edu.uptc.model;

import co.edu.uptc.persistencia.JsonRepository;

import java.io.File;

public class PartidaRepository {

    private static final String ARCHIVO = "partida.json";

    private final JsonRepository<Partida> repository;

    public PartidaRepository() {
        this(JsonRepository.CARPETA_DATOS);
    }

    public PartidaRepository(String carpeta) {
        this.repository = new JsonRepository<>(new File(carpeta, ARCHIVO).getPath(), Partida.class);
    }

    public boolean guardarPartida(Partida partida) {
        return repository.writeFile(partida);
    }

    public Partida cargarPartida() {
        return repository.readFile();
    }

    public boolean hayPartidaGuardada() {
        return repository.existe() && cargarPartida() != null;
    }

    public void borrarPartida() {
        repository.borrar();
    }
}
