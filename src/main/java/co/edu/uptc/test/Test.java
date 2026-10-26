package co.edu.uptc.test;

import co.edu.uptc.model.*;

import java.util.Random;
import java.util.Scanner;


public class Test {

    private final Scanner entrada = new Scanner(System.in);
    private final Random azar = new Random();
    private final JugadorRepository jugadores = new JugadorRepository();
    private final PartidaRepository partidas = new PartidaRepository();
    private final ConfiguracionRepository configuraciones = new ConfiguracionRepository();


    private Partida partida;

    private int pruebasOk;
    private int pruebasFallidas;

    public static void main(String[] args) {
        new Test().ejecutar();
    }

    private void ejecutar() {
        boolean seguir = true;
        while (seguir) {
            mostrarMenuPrincipal();
            int opcion = leerEntero("Opcion: ", 0, 6);
            switch (opcion) {
                case 1:
                    menuJugadores();
                    break;
                case 2:
                    menuConfiguracion();
                    break;
                case 3:
                    menuFlujo();
                    break;
                case 4:
                    menuPersistencia();
                    break;
                case 5:
                    pruebasAutomaticas();
                    break;
                case 6:
                    mostrarEstado();
                    break;
                default:
                    seguir = false;
            }
        }
        System.out.println("Fin de las pruebas.");
    }




    private void mostrarMenuPrincipal() {
        System.out.println();
        System.out.println("==================== MENU DE PRUEBAS ====================");
        System.out.println("1. Jugadores (perfiles y estadisticas)");
        System.out.println("2. Configuracion");
        System.out.println("3. Flujo de la partida");
        System.out.println("4. Guardar / cargar partida");
        System.out.println("5. Pruebas automáticas de persistencia");
        System.out.println("6. Estado actual");
        System.out.println("0. Salir");
    }

    private void menuJugadores() {
        boolean volver = false;
        while (!volver) {
            System.out.println();
            System.out.println("--- Jugadores ---");
            System.out.println("1. Crear o actualizar perfil");
            System.out.println("2. Listar perfiles");
            System.out.println("3. Ver estadisticas de un jugador");
            System.out.println("4. Ver ranking");
            System.out.println("5. Registrar resultado manual");
            System.out.println("0. Volver");
            switch (leerEntero("Opcion: ", 0, 5)) {
                case 1:
                    crearPerfil();
                    break;
                case 2:
                    listarPerfiles();
                    break;
                case 3:
                    verEstadisticas();
                    break;
                case 4:
                    verRanking();
                    break;
                case 5:
                    registrarResultadoManual();
                    break;
                default:
                    volver = true;
            }
        }
    }

    private void menuConfiguracion() {
        boolean volver = false;
        while (!volver) {
            System.out.println();
            System.out.println("--- Configuracion ---");
            System.out.println("1. Ver configuracion");
            System.out.println("2. Cambiar volumen de musica");
            System.out.println("3. Cambiar volumen de efectos");
            System.out.println("4. Activar / desactivar voz de mando");
            System.out.println("5. Cambiar idioma (es / en)");
            System.out.println("6. Guardar configuracion");
            System.out.println("0. Volver");
            Configuracion config = configuraciones.getConfiguracion();
            switch (leerEntero("Opción: ", 0, 6)) {
                case 1:
                    mostrarConfiguracion(config);
                    break;
                case 2:
                    config.setVolumenMusica(leerEntero("Volumen musica (0-100): ", 0, 100));
                    break;
                case 3:
                    config.setVolumenEfectos(leerEntero("Volumen efectos (0-100): ", 0, 100));
                    break;
                case 4:
                    config.setVozDeMando(!config.isVozDeMando());
                    System.out.println("Voz de mando: " + config.isVozDeMando());
                    break;
                case 5:
                    String idioma = leerTexto("Idioma (es/en): ");
                    System.out.println(config.setIdioma(idioma)
                            ? "Idioma cambiado a " + config.getIdioma()
                            : "Idioma no soportado; se conserva " + config.getIdioma());
                    break;
                case 6:
                    configuraciones.guardarConfiguracion(config);
                    System.out.println("Configuracion guardada.");
                    break;
                default:
                    volver = true;
            }
        }
    }

    private void menuFlujo() {
        boolean volver = false;
        while (!volver) {
            System.out.println();
            System.out.println("--- Flujo principal del juego ---");
            System.out.println("Etapa actual: " + etapaActual());
            System.out.println("1. Crear partida");
            System.out.println("2. Colocar barcos");
            System.out.println("3. Comenzar partida");
            System.out.println("4. Disparar (bucle)");
            System.out.println("5. Fin del juego");
            System.out.println("0. Volver");
            switch (leerEntero("Opcion: ", 0, 5)) {
                case 1:
                    crearPartida();
                    break;
                case 2:
                    colocarBarcos();
                    break;
                case 3:
                    comenzarPartida();
                    break;
                case 4:
                    disparar();
                    break;
                case 5:
                    finalizarPartida();
                    break;
                default:
                    volver = true;
            }
        }
    }

    private void menuPersistencia() {
        boolean volver = false;
        while (!volver) {
            System.out.println();
            System.out.println("--- Guardar / cargar partida (data/partida.json) ---");
            System.out.println("1. Guardar partida actual");
            System.out.println("2. Cargar partida guardada");
            System.out.println("3. Comprobar si hay partida guardada");
            System.out.println("4. Borrar partida guardada");
            System.out.println("0. Volver");
            switch (leerEntero("Opcion: ", 0, 4)) {
                case 1:
                    guardarPartidaActual();
                    break;
                case 2:
                    cargarPartidaGuardada();
                    break;
                case 3:
                    System.out.println(partidas.hayPartidaGuardada()
                            ? "Hay una partida guardada."
                            : "No hay partida guardada.");
                    break;
                case 4:
                    partidas.borrarPartida();
                    System.out.println("Partida guardada borrada.");
                    break;
                default:
                    volver = true;
            }
        }
    }




    private void crearPerfil() {
        String nombre = leerTexto("Nombre: ");
        String biografia = leerTexto("Biografia: ");
        int avatar = leerEntero("Avatar (0-" + (Jugador.cantidadAvatares - 1) + "): ",
                0, Jugador.cantidadAvatares - 1);
        Jugador jugador = jugadores.guardarJugador(nombre, biografia, avatar);
        if (jugador == null) {
            System.out.println("Nombre no valido ");
        } else {
            System.out.println("Perfil guardado: " + jugador.getNombre());
        }
    }

    private void listarPerfiles() {
        if (jugadores.getListaJugadores().isEmpty()) {
            System.out.println("No hay perfiles.");
        }
        for (Jugador jugador : jugadores.getListaJugadores()) {
            System.out.println(jugador.getNombre() + " | avatar " + jugador.getAvatar()
                    + " | " + jugador.getBiografia());
        }
    }

    private void verEstadisticas() {
        Jugador jugador = jugadores.buscarJugador(leerTexto("Nombre del jugador: "));
        if (jugador == null) {
            System.out.println("No existe ese jugador.");
        } else {
            mostrarEstadisticas(jugador);
        }
    }

    private void mostrarEstadisticas(Jugador jugador) {
        System.out.println("Estadisticas de " + jugador.getNombre() + ":");
        System.out.println("  Partidas jugadas:  " + jugador.getPartidasJugadas());
        System.out.println("  Ganadas:           " + jugador.getPartidasGanadas());
        System.out.println("  Perdidas:          " + jugador.getPartidasPerdidas());
        System.out.println("  Mejor (turnos):    " + (jugador.getMejorTurnos() == 0
                ? "sin victorias" : String.valueOf(jugador.getMejorTurnos())));
    }

    private void verRanking() {
        int posicion = 1;
        for (Jugador jugador : jugadores.getRanking()) {
            System.out.println(posicion++ + ". " + jugador.getNombre()
                    + " - mjor en " + jugador.getMejorTurnos() + " turnos");
        }
        if (posicion == 1) {
            System.out.println("El ranking esta vacio (nadie ha ganado aun).");
        }
    }

    private void registrarResultadoManual() {
        String nombre = leerTexto("Nombre del jugador: ");
        if (confirmar("¿Fue victoria? (s/n): ")) {
            int turnos = leerEntero("Turnos: ", 1, 500);
            System.out.println("Registrado: " + jugadores.registrarVictoria(nombre, turnos));
        } else {
            System.out.println("Registrado: " + jugadores.registrarDerrota(nombre));
        }
    }




    private void mostrarConfiguracion(Configuracion config) {
        System.out.println("Musica: " + config.getVolumenMusica()
                + " | Efectos: " + config.getVolumenEfectos()
                + " | Voz de mando: " + config.isVozDeMando()
                + " | Idioma: " + config.getIdioma());
    }



    private String etapaActual() {
        if (partida == null) {
            return "1. Crear partida (sin partida)";
        }
        switch (partida.getEtapa()) {
            case TERMINADA:
                return "5. Fin del juego (hay ganador: " + partida.getNombreGanador() + ")";
            case COLOCANDO_BARCOS:
                return "2. Colocar barcos (faltan barcos)";
            default:
                return "3/4. Partida lista o en curso (turno de " + partida.getNombreTurnoActual() + ")";
        }
    }

    private void crearPartida() {
        if (partida != null && !confirmar("Ya hay una partida. ¿Descartarla? (s/n): ")) {
            return;
        }
        System.out.println("1. Contra el computador");
        System.out.println("2. Dos jugadores");
        int modo = leerEntero("Modo: ", 1, 2);

        Jugador jugador1 = obtenerOCrearJugador("Jugador 1");
        if (jugador1 == null) {
            return;
        }
        try {
            if (modo == 1) {
                partida = new Partida(jugador1);
            } else {
                Jugador jugador2 = obtenerOCrearJugador("Jugador 2");
                if (jugador2 == null) {
                    return;
                }
                partida = new Partida(jugador1, jugador2);
            }
            System.out.println("Partida creada. Siguiente paso: colocar barcos.");
        } catch (IllegalArgumentException e) {
            System.out.println("No se pudo crear la partida: " + e.getMessage());
        }
    }



    private Jugador obtenerOCrearJugador(String etiqueta) {
        String nombre = leerTexto(etiqueta + " - nombre: ");
        Jugador jugador = jugadores.buscarJugador(nombre);
        if (jugador != null) {
            System.out.println("Perfil existente: " + jugador.getNombre());
            return jugador;
        }
        int avatar = leerEntero("Perfil nuevo. Avatar (0-" + (Jugador.cantidadAvatares - 1) + "): ",
                0, Jugador.cantidadAvatares - 1);
        jugador = jugadores.guardarJugador(nombre, "", avatar);
        if (jugador == null) {
            System.out.println("Nombre no válido.");
        }
        return jugador;
    }

    private void colocarBarcos() {
        if (!hayPartida()) {
            return;
        }
        System.out.println("1. Manual   2. Aleatorio");
        boolean manual = leerEntero("Modo: ", 1, 2) == 1;

        colocarFlota(partida.getTablero1(), partida.getJugador1().getNombre(), manual);
        if (!partida.isContraComputador()) {
            colocarFlota(partida.getTablero2(), partida.getJugador2().getNombre(), manual);
        } else {
            System.out.println("El computador ya tiene su flota colocada al azar.");
        }
        System.out.println("¿Listo para comenzar? " + partida.puedeComenzar());
    }

    private void colocarFlota(Tablero tablero, String nombre, boolean manual) {
        if (!manual) {
            tablero.colocarFlotaAleatoria();
            System.out.println("Flota de " + nombre + " colocada al azar.");
            mostrarTablero(tablero, true);
            return;
        }
        for (Barco barco : tablero.getBarcos()) {
            boolean colocado = false;
            while (!colocado) {
                System.out.println(nombre + ": colocar " + barco.getNombre()
                        + " (" + barco.getTamano() + " casillas)");
                mostrarTablero(tablero, true);
                int fila = leerEntero("Fila (0-9): ", 0, Tablero.tamano - 1);
                int columna = leerEntero("Columna (0-9): ", 0, Tablero.tamano - 1);
                boolean horizontal = confirmar("¿Horizontal? (s/n): ");
                colocado = tablero.colocarBarco(barco, fila, columna, horizontal);
                if (!colocado) {
                    System.out.println("Posición no válida (fuera del tablero o solapada).");
                }
            }
        }
    }

    private void comenzarPartida() {
        if (!hayPartida()) {
            return;
        }
        if (partida.hayGanador()) {
            System.out.println("La partida ya terminó.");
        } else if (!partida.puedeComenzar()) {
            System.out.println("Faltan barcos por colocar. Use el paso 2.");
        } else {
            System.out.println("Partida comenzada. Empieza " + partida.getNombreTurnoActual() + ".");
        }
    }

    private void disparar() {
        if (!hayPartida()) {
            return;
        }
        if (!partida.puedeComenzar()) {
            System.out.println("La partida no puede comenzar: coloque todos los barcos primero.");
            return;
        }
        if (partida.hayGanador()) {
            System.out.println("La partida ya terminó. Use el paso 5.");
            return;
        }
        System.out.println("Escriba -1 como fila para pausar y volver al menú.");
        while (!partida.hayGanador()) {
            if (partida.esTurnoComputador()) {
                int resultado = partida.disparoComputador();
                System.out.println("Computador dispara: " + nombreResultado(resultado));
                mostrarTablero(partida.getTablero1(), true);
            } else {
                Tablero rival = partida.getTableroRival();
                System.out.println("Turno de " + partida.getNombreTurnoActual() + ".");
                mostrarTablero(rival, false);
                int fila = leerEntero("Fila (0-9, -1 pausa): ", -1, Tablero.tamano - 1);
                if (fila == -1) {
                    System.out.println("Partida en pausa.");
                    return;
                }
                int columna = leerEntero("Columna (0-9): ", 0, Tablero.tamano - 1);
                int resultado = partida.disparar(fila, columna);
                if (resultado == Tablero.invalido) {
                    System.out.println("Disparo no válido (ya disparado en esa casilla).");
                    continue;
                }
                System.out.println("Resultado: " + nombreResultado(resultado));
                if (resultado == Tablero.hundido) {
                    System.out.println("Hundido: " + rival.getBarcoEn(fila, columna).getNombre());
                }
            }
        }
        finalizarPartida();
    }

    private void finalizarPartida() {
        if (partida == null) {
            System.out.println("No hay partida.");
            return;
        }
        if (!partida.hayGanador()) {
            System.out.println("La partida aún no tiene ganador.");
            return;
        }
        Jugador ganador = partida.getGanador();
        System.out.println("Ganador: " + ganador.getNombre()
                + " en " + partida.getTurnos(ganador) + " turnos.");
        System.out.println("Resultados registrados: " + jugadores.registrarResultadoPartida(partida));
        partidas.borrarPartida();
        System.out.println("Partida guardada eliminada (la partida terminó).");
        partida = null;
    }




    private void guardarPartidaActual() {
        if (!hayPartida()) {
            return;
        }
        boolean ok = partidas.guardarPartida(partida);
        System.out.println(ok ? "Partida guardada." : "Error al guardar (vea la consola).");
    }

    private void cargarPartidaGuardada() {
        if (partida != null && !confirmar("Se reemplazara la partida actual. ¿Continuar? (s/n): ")) {
            return;
        }
        Partida cargada = partidas.cargarPartida();
        if (cargada == null) {
            System.out.println("No se pudo cargar la partida.");
            return;
        }
        partida = cargada;
        System.out.println("Partida cargada. " + etapaActual());
        mostrarEstado();
    }




    private void mostrarEstado() {
        System.out.println("Etapa: " + etapaActual());
        if (partida == null) {
            return;
        }
        System.out.println("Jugador 1: " + partida.getJugador1().getNombre()
                + " (turnos: " + partida.getTurnos(partida.getJugador1()) + ")");
        System.out.println("Jugador 2: " + partida.getJugador2().getNombre()
                + " (turnos: " + partida.getTurnos(partida.getJugador2()) + ")"
                + (partida.isContraComputador() ? " [computador]" : ""));
        System.out.println("Modo: " + (partida.isContraComputador() ? "contra computador" : "dos jugadores"));
    }




    private void pruebasAutomaticas() {
        pruebasOk = 0;
        pruebasFallidas = 0;
        Partida guardadaPrevia = partidas.cargarPartida();
        System.out.println("ver los .json.");
        pruebaJugadores();
        pruebaConfiguracion();
        pruebaPartidaHumanos();
        pruebaPartidaComputador();
        pruebaReglasDelFlujo();
        restaurarPartidaGuardada(guardadaPrevia);
        System.out.println();
        System.out.println("Resultado: " + pruebasOk + " correctas, " + pruebasFallidas + " fallidas.");
    }

    private void pruebaJugadores() {
        System.out.println("\n[Jugadores] guardado y estadisticas tras recargar");
        String nombre = "PruebaJug" + System.currentTimeMillis();
        jugadores.guardarJugador(nombre, "bio de prueba", 2);
        jugadores.registrarVictoria(nombre, 9);
        jugadores.registrarVictoria(nombre, 5);
        jugadores.registrarDerrota(nombre);


        JugadorRepository lectura = new JugadorRepository();
        Jugador leido = lectura.buscarJugador(nombre);
        verificar("el perfil existe despues de recargar", leido != null);
        if (leido != null) {
            verificar("avatar conservado", leido.getAvatar() == 2);
            verificar("ganadas = 2", leido.getPartidasGanadas() == 2);
            verificar("perdidas = 1", leido.getPartidasPerdidas() == 1);
            verificar("jugadas = 3", leido.getPartidasJugadas() == 3);
            verificar("mejor turnos = 5", leido.getMejorTurnos() == 5);
        }
    }

    private void pruebaConfiguracion() {
        System.out.println("\n[Configuración] guardado y recarga");
        Configuracion config = configuraciones.getConfiguracion();
        config.setVolumenMusica(33);
        config.setVolumenEfectos(77);
        config.setVozDeMando(true);
        config.setIdioma("en");
        configuraciones.guardarConfiguracion(config);

        Configuracion leida = new ConfiguracionRepository().getConfiguracion();
        verificar("volumen música = 33", leida.getVolumenMusica() == 33);
        verificar("volumen efectos = 77", leida.getVolumenEfectos() == 77);
        verificar("voz de mando activa", leida.isVozDeMando());
        verificar("idioma = en", "en".equals(leida.getIdioma()));
    }

    private void pruebaPartidaHumanos() {
        System.out.println("\n[Partida dos jugadores] guardar, cargar y terminar");
        String ts = String.valueOf(System.currentTimeMillis());
        Partida original = new Partida(new Jugador("PruebaA" + ts, "", 0), new Jugador("PruebaB" + ts, "", 1));
        original.getTablero1().colocarFlotaAleatoria();
        original.getTablero2().colocarFlotaAleatoria();
        for (int i = 0; i < 15 && !original.hayGanador(); i++) {
            dispararAleatorio(original);
        }

        PartidaRepository repo = partidas;
        verificar("guardar devuelve true", repo.guardarPartida(original));
        Partida cargada = repo.cargarPartida();
        verificar("cargar devuelve partida", cargada != null);
        if (cargada == null) {
            return;
        }
        verificar("mismo turno", cargada.getNombreTurnoActual().equals(original.getNombreTurnoActual()));
        verificar("mismos turnos J1", cargada.getTurnos(cargada.getJugador1()) == original.getTurnos(original.getJugador1()));
        verificar("mismos turnos J2", cargada.getTurnos(cargada.getJugador2()) == original.getTurnos(original.getJugador2()));
        verificar("mismo tablero 1", original.getTablero1().tieneMismosDisparosQue(cargada.getTablero1()));
        verificar("mismo tablero 2", original.getTablero2().tieneMismosDisparosQue(cargada.getTablero2()));
        verificar("flota completa tras cargar", cargada.puedeComenzar());

        int guardia = 0;
        while (!cargada.hayGanador() && guardia++ < 1000) {
            dispararAleatorio(cargada);
        }
        verificar("la partida cargada se puede terminar", cargada.hayGanador());
    }

    private void pruebaPartidaComputador() {
        System.out.println("\n[Partida contra computador] turno del computador se conserva");
        Partida original = new Partida(new Jugador("PruebaPC" + System.currentTimeMillis(), "", 0));
        original.getTablero1().colocarFlotaAleatoria();
        for (int i = 0; i < 12 && !original.hayGanador(); i++) {
            if (original.esTurnoComputador()) {
                original.disparoComputador();
            } else {
                dispararAleatorio(original);
            }
        }

        PartidaRepository repo = partidas;
        repo.guardarPartida(original);
        Partida cargada = repo.cargarPartida();
        verificar("cargar devuelve partida", cargada != null);
        if (cargada == null) {
            return;
        }
        verificar("turno del computador conservado",
                cargada.esTurnoComputador() == original.esTurnoComputador());
        verificar("modo contra computador conservado", cargada.isContraComputador());
        verificar("mismo tablero del jugador", original.getTablero1().tieneMismosDisparosQue(cargada.getTablero1()));

        int guardia = 0;
        while (!cargada.hayGanador() && guardia++ < 5000) {
            if (cargada.esTurnoComputador()) {
                cargada.disparoComputador();
            } else {
                dispararAleatorio(cargada);
            }
        }
        verificar("la partida cargada contra computador termina", cargada.hayGanador());
    }

    private void pruebaReglasDelFlujo() {
        System.out.println("\nvalidaciones del modelo");
        Tablero tablero = new Tablero();
        verificar("barco ajeno al tablero se rechaza",
                !tablero.colocarBarco(new Acorazado(), 0, 0, true));
        Tablero vacio = new Tablero();
        verificar("disparo fuera de rango devuelve -1",
                vacio.recibirDisparo(10, 0) == Tablero.invalido);
        try {
            new Partida(new Jugador("Igual", "", 0), new Jugador("igual", "", 1));
            verificar("mismo nombre lanza excepcion", false);
        } catch (IllegalArgumentException e) {
            verificar("mismo nombre lanza excepcion", true);
        }
        Partida sinFlota = new Partida(new Jugador("Sin", "", 0), new Jugador("Flota", "", 1));
        verificar("no se puede disparar con flota incompleta", sinFlota.disparar(0, 0) == Tablero.invalido);
        verificar("getPerdedor es null sin ganador", sinFlota.getPerdedor() == null);
    }





    private void restaurarPartidaGuardada(Partida previa) {
        if (previa != null) {
            partidas.guardarPartida(previa);
            System.out.println("\nSe restauro la partida guardada.");
        } else {
            partidas.borrarPartida();
            System.out.println("\nSe elimino la partida de prueba.");
        }
    }



    private void verificar(String descripcion, boolean condicion) {
        if (condicion) {
            pruebasOk++;
            System.out.println("  [OK]     " + descripcion);
        } else {
            pruebasFallidas++;
            System.out.println("  [FALLO]  " + descripcion);
        }
    }

    private int dispararAleatorio(Partida p) {
        int[] casilla = p.getTableroRival().casillaSinDisparar(azar);
        if (casilla == null) {
            return Tablero.invalido;
        }
        return p.disparar(casilla[0], casilla[1]);
    }

    private boolean hayPartida() {
        if (partida == null) {
            System.out.println("No hay partida. Use el paso 1 primero.");
            return false;
        }
        return true;
    }



    private void mostrarTablero(Tablero tablero, boolean mostrarBarcos) {
        System.out.print("    ");
        for (int c = 0; c < Tablero.tamano; c++) {
            System.out.print(c + " ");
        }
        System.out.println();
        for (int f = 0; f < Tablero.tamano; f++) {
            System.out.print(f + "   ");
            for (int c = 0; c < Tablero.tamano; c++) {
                System.out.print(simboloCasilla(tablero, f, c, mostrarBarcos) + " ");
            }
            System.out.println();
        }
        System.out.println("  o = agua   X = tocado" + (mostrarBarcos ? "   P/A/C/D = barcos" : ""));
    }

    private char simboloCasilla(Tablero tablero, int f, int c, boolean mostrarBarcos) {
        int disparo = tablero.getDisparo(f, c);
        if (disparo == Tablero.agua) {
            return 'o';
        }
        if (disparo == Tablero.tocado) {
            return 'X';
        }
        if (mostrarBarcos) {
            Barco barco = tablero.getBarcoEn(f, c);
            if (barco != null) {
                return barco.getNombre().charAt(0);
            }
        }
        return '.';
    }

    private String nombreResultado(int resultado) {
        switch (resultado) {
            case Tablero.agua:
                return "AGUA";
            case Tablero.tocado:
                return "TOCADO";
            case Tablero.hundido:
                return "HUNDIDO";
            default:
                return "NO VÁLIDO";
        }
    }

    private int leerEntero(String mensaje, int minimo, int maximo) {
        while (true) {
            System.out.print(mensaje);
            String texto = entrada.nextLine().trim();
            try {
                int valor = Integer.parseInt(texto);
                if (valor >= minimo && valor <= maximo) {
                    return valor;
                }
            } catch (NumberFormatException e) {

            }
            System.out.println("Ingrese un número entre " + minimo + " y " + maximo + ".");
        }
    }

    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return entrada.nextLine().trim();
    }

    
    private boolean confirmar(String mensaje) {
        System.out.print(mensaje);
        String respuesta = entrada.nextLine().trim().toLowerCase();
        return respuesta.startsWith("s") || respuesta.startsWith("y");
    }
}
