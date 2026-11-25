package co.edu.uptc.view;

import co.edu.uptc.view.componentes.TableroVista;

import java.util.List;


public interface ControladorVista {

    // ---------------- configuracion ----------------

    int getVolumenMusica();

    int getVolumenEfectos();

    boolean isVozDeMando();

    String getIdioma();

    void setVolumenMusica(int volumen);

    void setVolumenEfectos(int volumen);

    void setVozDeMando(boolean activa);

    void setIdioma(String codigo);

    void guardarConfiguracion();

    // ---------------- perfiles y seleccion de jugadores ----------------


    PerfilVista getPerfil(int numero);


    ErrorPerfil guardarPerfil(int numero, String nombre, String biografia, int avatar);

    void iniciarSeleccion(int cantidadJugadores);


    boolean crearPartida();

    // ---------------- estadisticas ----------------

    List<FilaRankingVista> getRanking();


    EstadisticasJugadorVista getEstadisticas(String nombre);

    // ---------------- partida guardada ----------------

    boolean hayPartidaGuardada();

    boolean continuarPartida();

    // ---------------- estado de la partida ----------------

    boolean hayPartida();

    boolean isColocando();

    boolean isContraComputador();


    boolean isTurnoTerminado();

    boolean isTurnoComputador();

    boolean hayGanador();

    String getNombreGanador();

    boolean ganoElComputador();


    String getNombreJugadorVista();


    String consumirRelevoPendiente();

    TableroVista getTableroPropio();

    TableroVista getTableroEnemigo();

    // ---------------- acciones de juego ----------------

    boolean moverBarco(int indiceBarco, int fila, int columna, boolean horizontal);


    String confirmarColocacion();

    ResultadoDisparo disparar(int fila, int columna);


    String terminarTurno();

    ResultadoDisparo disparoComputador();


    void revancha();
}
