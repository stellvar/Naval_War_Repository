package co.edu.uptc.view.pantallas;

import co.edu.uptc.view.Audio;
import co.edu.uptc.view.ControladorVista;
import co.edu.uptc.view.IdPantalla;
import co.edu.uptc.view.Navegador;
import co.edu.uptc.view.ResultadoDisparo;
import co.edu.uptc.view.Tema;
import co.edu.uptc.view.Textos;
import co.edu.uptc.view.componentes.BarraInferior;
import co.edu.uptc.view.componentes.BotonIcono;
import co.edu.uptc.view.componentes.BotonMarino;
import co.edu.uptc.view.componentes.CabeceraPantalla;
import co.edu.uptc.view.componentes.DialogoBotones;
import co.edu.uptc.view.componentes.EstiloFlota;
import co.edu.uptc.view.componentes.EtiquetaTitulo;
import co.edu.uptc.view.componentes.Iconos;
import co.edu.uptc.view.componentes.PanelFlota;
import co.edu.uptc.view.componentes.PanelSonido;
import co.edu.uptc.view.componentes.TableroPanel;
import co.edu.uptc.view.componentes.TableroVista;
import co.edu.uptc.view.componentes.TarjetaModal;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

/*
 * colocación de la flota, combate por turnos, turno del computador y final.
 * Todo el estado vive en el controlador; esta pantalla solo lo dibuja y le avisa de las acciones.
 */
public class PantallaJuego extends Pantalla {

    private static final int PAUSA_COMPUTADOR_MS = 900;

    private BotonIcono engranaje;
    private JLabel mensaje;
    private TableroPanel tableroPropio;
    private TableroPanel tableroEnemigo;
    private PanelFlota flotaPropia;
    private PanelFlota flotaEnemiga;
    private Timer temporizadorComputador;

    public PantallaJuego(Navegador nav, ControladorVista ctrl) {
        super(nav, ctrl);
    }

    @Override
    protected void construir() {
        detenerComputador();
        if (!ctrl.hayPartida()) {
            SwingUtilities.invokeLater(() -> nav.ir(IdPantalla.MENU));
            return;
        }
        boolean colocando = ctrl.isColocando();
        String nombre = ctrl.getNombreJugadorVista().toUpperCase();

        add(new CabeceraPantalla(Textos.t(colocando ? "juego.colocar" : "juego.turno", nombre)), BorderLayout.NORTH);

        TableroVista propio = ctrl.getTableroPropio();
        TableroVista enemigo = ctrl.getTableroEnemigo();

        tableroPropio = new TableroPanel(EstiloFlota.PROPIO);
        tableroPropio.setTablero(propio, true);
        tableroPropio.setColocable(colocando);
        if (colocando) {
            tableroPropio.setOyenteColocacion((indice, fila, columna, horizontal) -> {
                if (!ctrl.moverBarco(indice, fila, columna, horizontal)) {
                    actualizar();
                }
            });
        }
        tableroEnemigo = new TableroPanel(EstiloFlota.ENEMIGO);
        tableroEnemigo.setTablero(enemigo, false);
        tableroEnemigo.setInteractivo(puedeDisparar());
        tableroEnemigo.setOyente(this::alDisparar);

        flotaPropia = new PanelFlota(EstiloFlota.PROPIO);
        flotaPropia.setTablero(propio);
        flotaEnemiga = new PanelFlota(EstiloFlota.ENEMIGO);
        flotaEnemiga.setTablero(enemigo);

        JPanel centro = new JPanel(new GridLayout(1, 2, 28, 0));
        centro.setOpaque(false);
        centro.setBorder(BorderFactory.createEmptyBorder(0, 44, 0, 44));
        centro.add(columna(Textos.t("juego.tuFlota"), EstiloFlota.PROPIO, tableroPropio, flotaPropia));
        centro.add(columna(Textos.t("juego.enemigo"), EstiloFlota.ENEMIGO, tableroEnemigo, flotaEnemiga));
        add(centro, BorderLayout.CENTER);

        engranaje = new BotonIcono(Iconos.de(Iconos.Tipo.ENGRANAJE, 34), 64);
        engranaje.addActionListener(e -> alternarSonido());
        mensaje = new JLabel(mensajeInicial(colocando), SwingConstants.CENTER);
        mensaje.setFont(Tema.fuente(Font.BOLD, 16));
        mensaje.setForeground(Tema.TEXTO);
        BotonMarino accion = new BotonMarino(Textos.t(colocando ? "comun.listo" : "juego.terminar"),
                Iconos.de(Iconos.Tipo.ANCLA, 28, Tema.ACCION_TEXTO), BotonMarino.Forma.CHAFLAN, 280, 52, 18).principal();
        accion.addActionListener(e -> {
            if (ctrl.isColocando()) {
                alConfirmarFlota();
            } else {
                alTerminarTurno();
            }
        });
        add(new BarraInferior(engranaje, mensaje, accion), BorderLayout.SOUTH);

        SwingUtilities.invokeLater(this::alEntrar);
    }

    /* tras dibujar: relevo pendiente (partida cargada) o turno del computador */
    private void alEntrar() {
        if (ctrl.hayGanador()) {
            return;
        }
        String relevo = ctrl.consumirRelevoPendiente();
        if (relevo != null) {
            mostrarRelevo(relevo);
        } else if (ctrl.isTurnoComputador()) {
            iniciarTurnoComputador();
        }
    }

    private boolean puedeDisparar() {
        return !ctrl.isColocando() && !ctrl.isTurnoTerminado() && !ctrl.isTurnoComputador() && !ctrl.hayGanador();
    }

    private String mensajeInicial(boolean colocando) {
        if (colocando) {
            return Textos.t("juego.colocarMsg");
        }
        if (ctrl.isTurnoComputador()) {
            return Textos.t("juego.computador");
        }
        return Textos.t(ctrl.isTurnoTerminado() ? "juego.agua" : "juego.dispara");
    }

    private JPanel columna(String titulo, EstiloFlota estilo, TableroPanel tablero, PanelFlota flota) {
        JPanel columna = new JPanel(new BorderLayout(0, 10));
        columna.setOpaque(false);
        columna.add(new EtiquetaTitulo(titulo, estilo), BorderLayout.NORTH);
        JPanel fila = new JPanel(new GridBagLayout());
        fila.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        c.insets = new Insets(0, 0, 0, 10);
        fila.add(tablero, c);
        c.weightx = 0;
        c.insets = new Insets(0, 0, 0, 0);
        c.gridx = 1;
        fila.add(flota, c);
        columna.add(fila, BorderLayout.CENTER);
        return columna;
    }

    /* vuelve a leer los tableros del controlador */
    private void actualizar() {
        TableroVista propio = ctrl.getTableroPropio();
        TableroVista enemigo = ctrl.getTableroEnemigo();
        tableroPropio.setTablero(propio, true);
        tableroEnemigo.setTablero(enemigo, false);
        flotaPropia.setTablero(propio);
        flotaEnemiga.setTablero(enemigo);
    }

    private void decir(String texto) {
        mensaje.setText(texto);
    }

    // ---------------- acciones del jugador ----------------

    private void alConfirmarFlota() {
        String relevo = ctrl.confirmarColocacion();
        if (relevo != null) {
            mostrarRelevo(relevo);
        } else {
            nav.recargar();
        }
    }

    private void alDisparar(int fila, int columna) {
        if (!puedeDisparar()) {
            return;
        }
        ResultadoDisparo resultado = ctrl.disparar(fila, columna);
        reproducirImpacto(resultado);
        actualizar();
        switch (resultado.tipo()) {
            case AGUA:
                tableroEnemigo.setInteractivo(false);
                decir(Textos.t("juego.agua"));
                break;
            case TOCADO:
                decir(Textos.t("juego.tocado"));
                break;
            case HUNDIDO:
                decir(Textos.t("juego.hundido", Textos.t("barco." + resultado.tamanoBarco())));
                break;
            default:
                decir(Textos.t("juego.repetida"));
        }
        if (ctrl.hayGanador()) {
            tableroEnemigo.setInteractivo(false);
            mostrarResultado();
        }
    }

    private void alTerminarTurno() {
        if (ctrl.hayGanador() || ctrl.isTurnoComputador()) {
            return;
        }
        if (!ctrl.isTurnoTerminado()) {
            decir(Textos.t("juego.dispara"));
            return;
        }
        String siguiente = ctrl.terminarTurno();
        if (siguiente != null) {
            mostrarRelevo(siguiente);
        } else {
            iniciarTurnoComputador();
        }
    }

    // ---------------- turno del computador ----------------

    private void iniciarTurnoComputador() {
        detenerComputador();
        tableroEnemigo.setInteractivo(false);
        decir(Textos.t("juego.computador"));
        temporizadorComputador = new Timer(PAUSA_COMPUTADOR_MS, e -> pasoComputador());
        temporizadorComputador.start();
    }

    private void pasoComputador() {
        ResultadoDisparo resultado = ctrl.disparoComputador();
        reproducirImpacto(resultado);
        actualizar();
        if (ctrl.hayGanador()) {
            detenerComputador();
            mostrarResultado();
            return;
        }
        switch (resultado.tipo()) {
            case AGUA:
                detenerComputador();
                tableroEnemigo.setInteractivo(true);
                decir(Textos.t("juego.compAgua"));
                break;
            case TOCADO:
                decir(Textos.t("juego.compTocado"));
                break;
            case HUNDIDO:
                decir(Textos.t("juego.compHundido", Textos.t("barco." + resultado.tamanoBarco())));
                break;
            default:
                detenerComputador();
        }
    }

    /* explosión cuando el disparo golpea un barco (tocado o hundido) */
    private void reproducirImpacto(ResultadoDisparo resultado) {
        if (resultado.tipo() == ResultadoDisparo.Tipo.TOCADO || resultado.tipo() == ResultadoDisparo.Tipo.HUNDIDO) {
            Audio.efectoExplosion();
        }
    }

    private void detenerComputador() {
        if (temporizadorComputador != null) {
            temporizadorComputador.stop();
            temporizadorComputador = null;
        }
    }

    // ---------------- ventanas emergentes ----------------

    private void alternarSonido() {
        if (nav.hayModal()) {
            nav.cerrarModal();
            return;
        }
        engranaje.setActivo(true);
        TarjetaModal tarjeta = new TarjetaModal();
        tarjeta.add(new PanelSonido(true, ctrl));
        tarjeta.add(Box.createVerticalStrut(22));
        tarjeta.add(botonMenuPartida(Textos.t("comun.continuar"), Iconos.Tipo.JUGAR, true, nav::cerrarModal));
        tarjeta.add(Box.createVerticalStrut(12));
        tarjeta.add(botonMenuPartida(Textos.t("comun.menu"), Iconos.Tipo.ANCLA, false, () -> {
            detenerComputador();
            nav.ir(IdPantalla.MENU);
        }));
        nav.mostrarModal(tarjeta, true, engranaje, Tema.VELO_OSCURO, () -> engranaje.setActivo(false));
    }

    private BotonMarino botonMenuPartida(String texto, Iconos.Tipo icono, boolean principal, Runnable accion) {
        BotonMarino boton = new BotonMarino(texto, Iconos.de(icono, 24, principal ? Tema.ACCION_TEXTO : Tema.CIAN),
                BotonMarino.Forma.CHAFLAN, 290, 49, 17);
        if (principal) {
            boton.principal();
        }
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.addActionListener(e -> accion.run());
        return boton;
    }

    /* "pasa el dispositivo a ...": al pulsar LISTO se redibuja la pantalla para el nuevo jugador */
    private void mostrarRelevo(String nombre) {
        DialogoBotones dialogo = new DialogoBotones(Textos.t("relevo.titulo", nombre.toUpperCase()));
        dialogo.setMensaje(Textos.t("relevo.msg", nombre));
        dialogo.agregarBotonPrincipal(Textos.t("comun.listo"), Iconos.de(Iconos.Tipo.ANCLA, 24, Tema.ACCION_TEXTO), () -> {
            nav.cerrarModal();
            nav.recargar();
        });
        nav.mostrarModal(dialogo, false, null, Tema.VELO_TAPADO, null);
    }

    private void mostrarResultado() {
        String titulo;
        if (ctrl.isContraComputador()) {
            titulo = Textos.t(ctrl.ganoElComputador() ? "final.perdiste" : "final.ganaste");
        } else {
            titulo = Textos.t("final.gano", ctrl.getNombreGanador().toUpperCase());
        }
        DialogoBotones dialogo = new DialogoBotones(titulo);
        dialogo.agregarBoton(Textos.t("comun.menu"), Iconos.de(Iconos.Tipo.ANCLA, 26, Tema.CIAN),
                () -> nav.ir(IdPantalla.MENU));
        dialogo.agregarBotonPrincipal(Textos.t("final.nueva"), Iconos.de(Iconos.Tipo.PISTOLA, 26, Tema.ACCION_TEXTO), () -> {
            ctrl.revancha();
            nav.ir(IdPantalla.JUEGO);
        });
        dialogo.agregarBoton(Textos.t("menu.salir"), Iconos.de(Iconos.Tipo.SALIDA, 26, Tema.CIAN), nav::salir);
        nav.mostrarModal(dialogo, false, null, Tema.VELO_OSCURO, null);
    }
}

