package co.edu.uptc.controller;

import co.edu.uptc.model.Barco;
import co.edu.uptc.model.Configuracion;
import co.edu.uptc.model.ConfiguracionRepository;
import co.edu.uptc.model.Impacto;
import co.edu.uptc.model.Jugador;
import co.edu.uptc.model.JugadorRepository;
import co.edu.uptc.model.Partida;
import co.edu.uptc.model.PartidaRepository;
import co.edu.uptc.model.Tablero;
import co.edu.uptc.view.ControladorVista;
import co.edu.uptc.view.ErrorPerfil;
import co.edu.uptc.view.EstadisticasJugadorVista;
import co.edu.uptc.view.FilaRankingVista;
import co.edu.uptc.view.MainVista;
import co.edu.uptc.view.PerfilVista;
import co.edu.uptc.view.ResultadoDisparo;
import co.edu.uptc.view.componentes.TableroVista;

import java.util.ArrayList;
import java.util.List;


public class Controller implements ControladorVista {

    private enum Fase { SIN_PARTIDA, COLOCANDO, COMBATE, TERMINADA }

    private final JugadorRepository jugadores;
    private final PartidaRepository partidas;
    private final ConfiguracionRepository configuraciones;

    private Partida partida;
    private Fase fase = Fase.SIN_PARTIDA;

    private int colocando = 1;
    private int vista = 1;
    private boolean turnoTerminado;
    private boolean relevoPendiente;
    private boolean resultadoRegistrado;

    private int cantidadSeleccion;
    private final Jugador[] elegidos = new Jugador[2];
    private final PerfilVista[] recordados = new PerfilVista[2];

    public Controller() {
        this.jugadores = new JugadorRepository();
        this.partidas = new PartidaRepository();
        this.configuraciones = new ConfiguracionRepository();
    }

    public void iniciar() {
        MainVista.iniciar(this);
    }

    // ================= configuracion =================

    @Override
    public int getVolumenMusica() {
        return config().getVolumenMusica();
    }

    @Override
    public int getVolumenEfectos() {
        return config().getVolumenEfectos();
    }

    @Override
    public boolean isVozDeMando() {
        return config().isVozDeMando();
    }

    @Override
    public String getIdioma() {
        return config().getIdioma();
    }

    @Override
    public void setVolumenMusica(int volumen) {
        config().setVolumenMusica(volumen);
    }

    @Override
    public void setVolumenEfectos(int volumen) {
        config().setVolumenEfectos(volumen);
    }

    @Override
    public void setVozDeMando(boolean activa) {
        config().setVozDeMando(activa);
    }

    @Override
    public void setIdioma(String codigo) {
        config().setIdioma(codigo);
    }

    @Override
    public void guardarConfiguracion() {
        configuraciones.guardarConfiguracion(config());
    }

    private Configuracion config() {
        return configuraciones.getConfiguracion();
    }

    // ================= perfiles y seleccion =================

    @Override
    public PerfilVista getPerfil(int numero) {
        return indiceValido(numero) ? recordados[numero - 1] : null;
    }

    @Override
    public ErrorPerfil guardarPerfil(int numero, String nombre, String biografia, int avatar) {
        if (!indiceValido(numero)) {
            return ErrorPerfil.NOMBRE_NO_VALIDO;
        }
        String limpio = nombre == null ? "" : nombre.trim();
        if (limpio.isEmpty() || limpio.equalsIgnoreCase(Partida.NOMBRE_COMPUTADOR)) {
            return ErrorPerfil.NOMBRE_NO_VALIDO;
        }
        boolean eligiendoDos = cantidadSeleccion == 2 && numero == 2;
        if (eligiendoDos && elegidos[0] != null && elegidos[0].getNombre().equalsIgnoreCase(limpio)) {
            return ErrorPerfil.NOMBRES_IGUALES;
        }
        Jugador jugador = jugadores.guardarJugador(limpio, biografia, avatar);
        if (jugador == null) {
            return ErrorPerfil.NOMBRE_NO_VALIDO;
        }
        recordados[numero - 1] = new PerfilVista(jugador.getNombre(), jugador.getBiografia(), jugador.getAvatar());
        if (cantidadSeleccion > 0) {
            elegidos[numero - 1] = jugador;
        }
        return null;
    }

    @Override
    public void iniciarSeleccion(int cantidadJugadores) {
        cantidadSeleccion = cantidadJugadores == 2 ? 2 : 1;
        elegidos[0] = null;
        elegidos[1] = null;
    }

    @Override
    public boolean crearPartida() {
        if (cantidadSeleccion == 0 || elegidos[0] == null || (cantidadSeleccion == 2 && elegidos[1] == null)) {
            return false;
        }
        try {
            partida = cantidadSeleccion == 1
                    ? new Partida(elegidos[0].getNombre())
                    : new Partida(elegidos[0].getNombre(), elegidos[1].getNombre());
        } catch (IllegalArgumentException e) {
            return false;
        }
        cantidadSeleccion = 0;
        elegidos[0] = null;
        elegidos[1] = null;
        prepararColocacion();
        return true;
    }

    private static boolean indiceValido(int numero) {
        return numero == 1 || numero == 2;
    }

    // ================= estadisticas =================

    @Override
    public List<FilaRankingVista> getRanking() {
        List<FilaRankingVista> filas = new ArrayList<>();
        for (Jugador jugador : jugadores.getRanking()) {
            filas.add(new FilaRankingVista(jugador.getNombre(), jugador.getAvatar(), jugador.getMejorTurnos()));
        }
        return filas;
    }

    @Override
    public EstadisticasJugadorVista getEstadisticas(String nombre) {
        Jugador jugador = jugadores.buscarJugador(nombre);
        if (jugador == null) {
            return null;
        }
        return new EstadisticasJugadorVista(jugador.getNombre(), jugador.getBiografia(), jugador.getAvatar(),
                jugador.getPartidasJugadas(), jugador.getPartidasGanadas(), jugador.getPartidasPerdidas(),
                jugador.getMejorTurnos());
    }

    // ================= partida guardada =================

    @Override
    public boolean hayPartidaGuardada() {
        return partidas.hayPartidaGuardada();
    }

    @Override
    public boolean continuarPartida() {
        Partida cargada = partidas.cargarPartida();
        if (cargada == null || cargada.hayGanador()) {
            partidas.borrarPartida();
            return false;
        }
        partida = cargada;
        if (!partida.getTablero1().estaListo()) {
            partida.getTablero1().colocarFlotaAleatoria();
        }
        if (!partida.getTablero2().estaListo()) {
            partida.getTablero2().colocarFlotaAleatoria();
        }
        fase = Fase.COMBATE;
        turnoTerminado = false;
        resultadoRegistrado = false;
        vista = partida.isContraComputador() ? 1 : numeroJugadorActual();
        relevoPendiente = !partida.isContraComputador();
        return true;
    }

    // ================= estado de la partida =================

    @Override
    public boolean hayPartida() {
        return partida != null && fase != Fase.SIN_PARTIDA;
    }

    @Override
    public boolean isColocando() {
        return hayPartida() && fase == Fase.COLOCANDO;
    }

    @Override
    public boolean isContraComputador() {
        return partida != null && partida.isContraComputador();
    }

    @Override
    public boolean isTurnoTerminado() {
        return fase == Fase.COMBATE && turnoTerminado;
    }

    @Override
    public boolean isTurnoComputador() {
        return fase == Fase.COMBATE && partida.isContraComputador()
                && partida.esTurnoComputador() && !turnoTerminado && !partida.hayGanador();
    }

    @Override
    public boolean hayGanador() {
        return partida != null && partida.hayGanador();
    }

    @Override
    public String getNombreGanador() {
        return partida == null ? null : partida.getNombreGanador();
    }

    @Override
    public boolean ganoElComputador() {
        return partida != null && partida.isContraComputador() && partida.hayGanador()
                && partida.getNombreGanador().equalsIgnoreCase(partida.getNombreJugador2());
    }

    @Override
    public String getNombreJugadorVista() {
        if (partida == null) {
            return "";
        }
        int numero = fase == Fase.COLOCANDO ? colocando : vista;
        return nombreJugador(numero);
    }

    @Override
    public String consumirRelevoPendiente() {
        if (!relevoPendiente || partida == null) {
            return null;
        }
        relevoPendiente = false;
        return nombreJugador(vista);
    }

    @Override
    public TableroVista getTableroPropio() {
        return aVista(tableroDe(fase == Fase.COLOCANDO ? colocando : vista));
    }

    @Override
    public TableroVista getTableroEnemigo() {
        return aVista(tableroDe(otro(fase == Fase.COLOCANDO ? colocando : vista)));
    }

    // ================= acciones de juego =================

    @Override
    public boolean moverBarco(int indiceBarco, int fila, int columna, boolean horizontal) {
        if (fase != Fase.COLOCANDO) {
            return false;
        }
        Tablero tablero = tableroDe(colocando);
        Barco[] barcos = tablero.getBarcos();
        if (indiceBarco < 0 || indiceBarco >= barcos.length) {
            return false;
        }
        return tablero.colocarBarco(barcos[indiceBarco], fila, columna, horizontal);
    }

    @Override
    public String confirmarColocacion() {
        if (fase != Fase.COLOCANDO || !tableroDe(colocando).estaListo()) {
            return null;
        }
        if (partida.isContraComputador()) {
            iniciarCombate();
            return null;
        }
        if (colocando == 1) {
            colocando = 2;
            return partida.getNombreJugador2();
        }
        iniciarCombate();
        return partida.getNombreJugador1();
    }

    @Override
    public ResultadoDisparo disparar(int fila, int columna) {
        if (fase != Fase.COMBATE || turnoTerminado || partida.hayGanador() || partida.esTurnoComputador()) {
            return ResultadoDisparo.de(ResultadoDisparo.Tipo.INVALIDO);
        }
        Tablero rival = partida.getTableroRival();
        boolean[] hundidosAntes = hundidos(rival);
        Impacto impacto = partida.disparar(fila, columna);
        return procesarDisparo(impacto, rival, hundidosAntes, false);
    }

    @Override
    public String terminarTurno() {
        if (fase != Fase.COMBATE || !turnoTerminado) {
            return null;
        }
        turnoTerminado = false;
        if (partida.isContraComputador()) {
            guardar();
            return null;
        }
        vista = numeroJugadorActual();
        guardar();
        return partida.getNombreTurnoActual();
    }

    @Override
    public ResultadoDisparo disparoComputador() {
        if (!isTurnoComputador()) {
            return ResultadoDisparo.de(ResultadoDisparo.Tipo.INVALIDO);
        }
        Tablero rival = partida.getTableroRival();
        boolean[] hundidosAntes = hundidos(rival);
        Impacto impacto = partida.disparoComputador();
        return procesarDisparo(impacto, rival, hundidosAntes, true);
    }

    @Override
    public void revancha() {
        if (partida == null) {
            return;
        }
        String jugador1 = partida.getNombreJugador1();
        String jugador2 = partida.getNombreJugador2();
        boolean contraComputador = partida.isContraComputador();
        partida = contraComputador ? new Partida(jugador1) : new Partida(jugador1, jugador2);
        prepararColocacion();
    }

    // ================= logica interna =================

    private void prepararColocacion() {
        partida.getTablero1().colocarFlotaAleatoria();
        if (!partida.isContraComputador()) {
            partida.getTablero2().colocarFlotaAleatoria();
        }
        fase = Fase.COLOCANDO;
        colocando = 1;
        vista = 1;
        turnoTerminado = false;
        relevoPendiente = false;
        resultadoRegistrado = false;
    }

    private void iniciarCombate() {
        fase = Fase.COMBATE;
        vista = 1;
        turnoTerminado = false;
        guardar();
    }

    private ResultadoDisparo procesarDisparo(Impacto impacto, Tablero rival, boolean[] hundidosAntes,
                                             boolean delComputador) {
        ResultadoDisparo resultado;
        switch (impacto) {
            case AGUA:
                resultado = ResultadoDisparo.de(ResultadoDisparo.Tipo.AGUA);
                if (!delComputador) {
                    turnoTerminado = true;
                }
                break;
            case TOCADO:
                resultado = ResultadoDisparo.de(ResultadoDisparo.Tipo.TOCADO);
                break;
            case HUNDIDO:
                resultado = new ResultadoDisparo(ResultadoDisparo.Tipo.HUNDIDO,
                        tamanoRecienHundido(rival, hundidosAntes));
                break;
            default:
                return ResultadoDisparo.de(ResultadoDisparo.Tipo.INVALIDO);
        }
        if (partida.hayGanador()) {
            finalizarPartida();
        } else {
            guardar();
        }
        return resultado;
    }

    private void finalizarPartida() {
        if (resultadoRegistrado) {
            return;
        }
        resultadoRegistrado = true;
        fase = Fase.TERMINADA;
        turnoTerminado = false;
        jugadores.registrarResultadoPartida(partida);
        partidas.borrarPartida();
    }

    private void guardar() {
        if (partida != null && !partida.hayGanador()) {
            partidas.guardarPartida(partida);
        }
    }

    private static boolean[] hundidos(Tablero tablero) {
        Barco[] barcos = tablero.getBarcos();
        boolean[] estado = new boolean[barcos.length];
        for (int i = 0; i < barcos.length; i++) {
            estado[i] = barcos[i].estaHundido();
        }
        return estado;
    }

    private static int tamanoRecienHundido(Tablero tablero, boolean[] hundidosAntes) {
        Barco[] barcos = tablero.getBarcos();
        for (int i = 0; i < barcos.length; i++) {
            if (barcos[i].estaHundido() && !hundidosAntes[i]) {
                return barcos[i].getTamano();
            }
        }
        return 0;
    }

    private String nombreJugador(int numero) {
        return numero == 1 ? partida.getNombreJugador1() : partida.getNombreJugador2();
    }

    private Tablero tableroDe(int numero) {
        return numero == 1 ? partida.getTablero1() : partida.getTablero2();
    }

    private static int otro(int numero) {
        return numero == 1 ? 2 : 1;
    }

    private int numeroJugadorActual() {
        return partida.getNombreTurnoActual().equalsIgnoreCase(partida.getNombreJugador1()) ? 1 : 2;
    }

    private static TableroVista aVista(Tablero tablero) {
        Barco[] barcos = tablero.getBarcos();
        TableroVista.Nave[] naves = new TableroVista.Nave[barcos.length];
        for (int i = 0; i < barcos.length; i++) {
            Barco barco = barcos[i];
            boolean[] impactos = barco.getImpactos();
            int cantidad = 0;
            for (boolean impacto : impactos) {
                if (impacto) {
                    cantidad++;
                }
            }
            int[] segmentos = new int[cantidad];
            int k = 0;
            for (int s = 0; s < impactos.length; s++) {
                if (impactos[s]) {
                    segmentos[k++] = s;
                }
            }
            naves[i] = new TableroVista.Nave(barco.getTamano(), barco.getFila(), barco.getColumna(),
                    barco.isHorizontal(), segmentos);
        }
        int[][] disparos = new int[Tablero.TAMANO][Tablero.TAMANO];
        for (int f = 0; f < Tablero.TAMANO; f++) {
            for (int c = 0; c < Tablero.TAMANO; c++) {
                if (tablero.sinDisparar(f, c)) {
                    disparos[f][c] = TableroVista.SIN_DISPARAR;
                } else {
                    disparos[f][c] = tablero.esImpacto(f, c) ? TableroVista.TOCADO : TableroVista.AGUA;
                }
            }
        }
        return TableroVista.crear(naves, disparos);
    }
}
