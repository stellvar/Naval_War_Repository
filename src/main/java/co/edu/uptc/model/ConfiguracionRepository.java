package co.edu.uptc.model;
import co.edu.uptc.persistance.JsonRepository;

public class ConfiguracionRepository {
    private JsonRepository<Configuracion> repository;
    private Configuracion miConfiguracion;

    public ConfiguracionRepository() {
        this.repository = new JsonRepository<>("src/data/configuracion.json", Configuracion.class);
    }
}

