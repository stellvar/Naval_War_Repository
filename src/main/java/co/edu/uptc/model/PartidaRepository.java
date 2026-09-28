package co.edu.uptc.model;
import co.edu.uptc.persistance.JsonRepository;

public class PartidaRepository {
    private JsonRepository<Partida> repository;

    public PartidaRepository() {
        this.repository = new JsonRepository<>("src/data/partida.json", Partida.class);
    }

    public void guardarPartida(Partida partida) {
        repository.writeFile(partida);
    }

    public Partida cargarPartida() {
        return repository.readFile();
    }

    public boolean hayPartidaGuardada() {
        return repository.readFile() != null;
    }

    public void borrarPartida() {
        repository.writeFile(null);
    }
}
