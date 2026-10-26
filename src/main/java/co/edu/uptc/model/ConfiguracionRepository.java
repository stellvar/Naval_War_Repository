package co.edu.uptc.model;

import co.edu.uptc.persistencia.JsonRepository;

import java.io.File;

public class ConfiguracionRepository {

    private static final String ARCHIVO = "configuracion.json";

    private final JsonRepository<Configuracion> repository;
    private Configuracion miConfiguracion;

    public ConfiguracionRepository() {
        this(JsonRepository.CARPETA_DATOS);
    }

    public ConfiguracionRepository(String carpeta) {
        this.repository = new JsonRepository<>(new File(carpeta, ARCHIVO).getPath(), Configuracion.class);
        Configuracion cargada = repository.readFile();
        this.miConfiguracion = cargada != null ? cargada : new Configuracion();
    }

    public Configuracion getConfiguracion() {
        return miConfiguracion;
    }

    public void guardarConfiguracion(Configuracion nueva) {
        this.miConfiguracion = nueva;
        repository.writeFile(nueva);
    }
}
