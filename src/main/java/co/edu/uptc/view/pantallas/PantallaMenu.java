package co.edu.uptc.view.pantallas;

import co.edu.uptc.view.IdPantalla;
import co.edu.uptc.view.ControladorVista;
import co.edu.uptc.view.Navegador;
import co.edu.uptc.view.Tema;
import co.edu.uptc.view.Textos;
import co.edu.uptc.view.componentes.BotonMarino;
import co.edu.uptc.view.componentes.CabeceraPantalla;
import co.edu.uptc.view.componentes.DialogoBotones;
import co.edu.uptc.view.componentes.Iconos;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;

/* "BATALLA NAVAL": JUGAR, OPCIONES, ESTADÍSTICAS, GUÍA y SALIR. */
public class PantallaMenu extends Pantalla {

    public PantallaMenu(Navegador nav, ControladorVista ctrl) {
        super(nav, ctrl);
    }

    @Override
    protected void construir() {
        add(new CabeceraPantalla(Textos.t("menu.titulo"), 56, 252), BorderLayout.NORTH);

        JPanel columna = new JPanel();
        columna.setOpaque(false);
        columna.setLayout(new BoxLayout(columna, BoxLayout.Y_AXIS));
        columna.add(boton("comun.jugar", Iconos.Tipo.JUGAR, 470, 78, 30, true, this::alJugar));
        columna.add(Box.createVerticalStrut(14));
        columna.add(boton("menu.opciones", Iconos.Tipo.ENGRANAJE, 380, 62, 23, false, () -> nav.ir(IdPantalla.OPCIONES)));
        columna.add(Box.createVerticalStrut(12));
        columna.add(boton("menu.estadisticas", Iconos.Tipo.BARRAS, 380, 62, 23, false, () -> nav.ir(IdPantalla.ESTADISTICAS)));
        columna.add(Box.createVerticalStrut(12));
        columna.add(boton("menu.guia", Iconos.Tipo.LIBRO, 380, 62, 23, false, () -> nav.ir(IdPantalla.GUIA)));
        columna.add(Box.createVerticalStrut(12));
        columna.add(boton("menu.salir", Iconos.Tipo.SALIDA, 380, 62, 23, false, nav::salir));
        JPanel centro = new JPanel(new GridBagLayout());
        centro.setOpaque(false);
        centro.add(columna);
        add(centro, BorderLayout.CENTER);
    }

    private BotonMarino boton(String clave, Iconos.Tipo icono, int ancho, int alto, float fuente,
                              boolean principal, Runnable accion) {
        BotonMarino boton = new BotonMarino(Textos.t(clave),
                Iconos.de(icono, (int) (alto * 0.44), principal ? Tema.ACCION_TEXTO : Tema.TEXTO),
                BotonMarino.Forma.CHAFLAN, ancho, alto, fuente).conSeparador();
        if (principal) {
            boton.principal();
        }
        boton.setAlignmentX(CENTER_ALIGNMENT);
        boton.addActionListener(e -> accion.run());
        return boton;
    }

    /* ventana de partida guardada */
    private void alJugar() {
        if (!ctrl.hayPartidaGuardada()) {
            nav.ir(IdPantalla.SELECCION);
            return;
        }
        DialogoBotones dialogo = new DialogoBotones(Textos.t("guardada.titulo"));
        dialogo.setMensaje(Textos.t("guardada.msg"));
        dialogo.agregarBotonPrincipal(Textos.t("guardada.continuar"), Iconos.de(Iconos.Tipo.JUGAR, 24, Tema.ACCION_TEXTO), () -> {
            nav.cerrarModal();
            nav.ir(ctrl.continuarPartida() ? IdPantalla.JUEGO : IdPantalla.SELECCION);
        });
        dialogo.agregarBoton(Textos.t("guardada.nueva"), Iconos.de(Iconos.Tipo.PISTOLA, 24, Tema.CIAN), () -> {
            nav.cerrarModal();
            nav.ir(IdPantalla.SELECCION);
        });
        dialogo.agregarBoton(Textos.t("comun.menu"), Iconos.de(Iconos.Tipo.ANCLA, 24, Tema.CIAN), nav::cerrarModal);
        nav.mostrarModal(dialogo, true, null, Tema.VELO_OSCURO, null);
    }
}
