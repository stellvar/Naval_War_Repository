package co.edu.uptc.view;

import co.edu.uptc.view.componentes.TableroVista;

import java.util.List;

/*
 * Contrato que la vista necesita del controlador. La vista solo conoce esta
 * interfaz y los tipos de co.edu.uptc.view: nunca importa el modelo.
 */
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

    /* ultimo perfil guardado para el jugador 1 o 2; null si no hay */
    PerfilVista getPerfil(int numero);

    /* devuelve null si todo salio bien o la clave i18n del error */
    String guardarPerfil(int numero, String nombre, String biografia, int avatar);

    void iniciarSeleccion(int cantidadJugadores);

    /* crea la partida con los perfiles elegidos; false si no se pudo */
    boolean crearPartida();

    // ---------------- estadisticas ----------------

    List<FilaRankingVista> getRanking();

    /* estadisticas completas de un jugador; null si no existe */
    EstadisticasJugadorVista getEstadisticas(String nombre);

    // ---------------- partida guardada ----------------

    boolean hayPartidaGuardada();

    boolean continuarPartida();

    // ---------------- estado de la partida ----------------

    boolean hayPartida();

    boolean isColocando();

    boolean isContraComputador();

    /* el jugador disparo al agua y falta pulsar TERMINAR TURNO */
    boolean isTurnoTerminado();

    boolean isTurnoComputador();

    boolean hayGanador();

    String getNombreGanador();

    boolean ganoElComputador();

    /* nombre del jugador que tiene el dispositivo ahora mismo */
    String getNombreJugadorVista();

    /* si hay que pasar el dispositivo devuelve el nombre a quien (una sola vez); si no, null */
    String consumirRelevoPendiente();

    TableroVista getTableroPropio();

    TableroVista getTableroEnemigo();

    // ---------------- acciones de juego ----------------

    boolean moverBarco(int indiceBarco, int fila, int columna, boolean horizontal);

    /* confirma la flota; devuelve el nombre del jugador a quien pasar el dispositivo o null */
    String confirmarColocacion();

    ResultadoDisparo disparar(int fila, int columna);

    /* termina el turno; devuelve el nombre del siguiente jugador (2 jugadores) o null (computador) */
    String terminarTurno();

    ResultadoDisparo disparoComputador();

    /* nueva partida con los mismos jugadores */
    void revancha();
}
