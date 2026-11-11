package co.edu.uptc.view.pantallas;

import co.edu.uptc.view.IdPantalla;
import co.edu.uptc.view.Navegador;
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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

/*flota, campo enemigo, leyendas de flota, engranaje de sonido y TERMINAR TURNO
 */
public class PantallaJuego extends Pantalla {

    private static final String JUGADOR = "Player1";

    private BotonIcono engranaje;

    public PantallaJuego(Navegador nav) {
        super(nav);
    }

    @Override
    protected void construir() {
        CabeceraPantalla cabecera = new CabeceraPantalla(Textos.t("juego.turno", JUGADOR.toUpperCase()));
        add(cabecera, BorderLayout.NORTH);

        TableroVista propio = TableroVista.demoPropio();
        TableroVista enemigo = TableroVista.demoEnemigo();

        TableroPanel tableroPropio = new TableroPanel(EstiloFlota.PROPIO);
        tableroPropio.setTablero(propio, true);
        tableroPropio.setColocable(true);
        TableroPanel tableroEnemigo = new TableroPanel(EstiloFlota.ENEMIGO);
        tableroEnemigo.setTablero(enemigo, false);
        tableroEnemigo.setInteractivo(true);
        tableroEnemigo.setOyente((fila, columna) -> mostrarResultado());

        PanelFlota flotaPropia = new PanelFlota(EstiloFlota.PROPIO);
        flotaPropia.setTablero(propio);
        PanelFlota flotaEnemiga = new PanelFlota(EstiloFlota.ENEMIGO);
        flotaEnemiga.setTablero(enemigo);

        JPanel centro = new JPanel(new GridLayout(1, 2, 28, 0));
        centro.setOpaque(false);
        centro.setBorder(BorderFactory.createEmptyBorder(0, 44, 0, 44));
        centro.add(columna(Textos.t("juego.tuFlota"), EstiloFlota.PROPIO, tableroPropio, flotaPropia));
        centro.add(columna(Textos.t("juego.enemigo"), EstiloFlota.ENEMIGO, tableroEnemigo, flotaEnemiga));
        add(centro, BorderLayout.CENTER);

        engranaje = new BotonIcono(Iconos.de(Iconos.Tipo.ENGRANAJE, 36), 56);
        engranaje.addActionListener(e -> alternarSonido());
        JLabel mensaje = new JLabel(Textos.t("juego.dispara"), SwingConstants.CENTER);
        mensaje.setFont(Tema.fuente(Font.BOLD, 16));
        mensaje.setForeground(Tema.TEXTO);
        BotonMarino terminar = new BotonMarino(Textos.t("juego.terminar"), Iconos.de(Iconos.Tipo.ANCLA, 28),
                BotonMarino.Forma.PILDORA, 310, 58, 20);
        terminar.addActionListener(e -> mostrarRelevo(JUGADOR));
        add(new BarraInferior(engranaje, mensaje, terminar), BorderLayout.SOUTH);
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

    // ---------------- ventanas emergentes ----------------

    private void alternarSonido() {
        if (nav.hayModal()) {
            nav.cerrarModal();
            return;
        }
        engranaje.setActivo(true);
        TarjetaModal tarjeta = new TarjetaModal();
        tarjeta.add(new PanelSonido(true));
        nav.mostrarModal(tarjeta, true, engranaje, Tema.VELO_CLARO, () -> engranaje.setActivo(false));
    }


    private void mostrarRelevo(String nombre) {
        DialogoBotones dialogo = new DialogoBotones(Textos.t("relevo.titulo", nombre.toUpperCase()));
        dialogo.setMensaje(Textos.t("relevo.msg", nombre));
        dialogo.agregarBoton(Textos.t("comun.listo"), Iconos.de(Iconos.Tipo.ANCLA, 24, Tema.CIAN), nav::cerrarModal);
        nav.mostrarModal(dialogo, false, null, Tema.VELO_OSCURO, null);
    }


    private void mostrarResultado() {
        DialogoBotones dialogo = new DialogoBotones(Textos.t("final.ganaste"));
        dialogo.agregarBoton(Textos.t("comun.menu"), Iconos.de(Iconos.Tipo.ANCLA, 26, Tema.CIAN),
                () -> nav.ir(IdPantalla.MENU));
        dialogo.agregarBoton(Textos.t("final.nueva"), Iconos.de(Iconos.Tipo.PISTOLA, 26, Tema.CIAN),
                () -> nav.ir(IdPantalla.JUEGO));
        dialogo.agregarBoton(Textos.t("menu.salir"), Iconos.de(Iconos.Tipo.SALIDA, 26, Tema.CIAN), nav::salir);
        nav.mostrarModal(dialogo, true, null, Tema.VELO_CLARO, null);
    }
}
