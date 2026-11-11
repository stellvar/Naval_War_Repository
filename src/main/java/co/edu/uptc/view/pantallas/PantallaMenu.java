package co.edu.uptc.view.pantallas;

import co.edu.uptc.view.IdPantalla;
import co.edu.uptc.view.Navegador;
import co.edu.uptc.view.Tema;
import co.edu.uptc.view.Textos;
import co.edu.uptc.view.componentes.BotonMarino;
import co.edu.uptc.view.componentes.CabeceraPantalla;
import co.edu.uptc.view.componentes.DialogoBotones;
import co.edu.uptc.view.componentes.Iconos;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.BorderLayout;

/* "BATALLA NAVAL": JUGAR, OPCIONES, ESTADÍSTICAS, GUÍA y SALIR. */
public class PantallaMenu extends Pantalla {

    public PantallaMenu(Navegador nav) {
        super(nav);
    }

    @Override
    protected void construir() {
        add(new CabeceraPantalla(Textos.t("menu.titulo")), BorderLayout.NORTH);

        JPanel columna = new JPanel();
        columna.setOpaque(false);
        columna.setLayout(new BoxLayout(columna, BoxLayout.Y_AXIS));
        columna.add(boton("comun.jugar", Iconos.Tipo.JUGAR, 470, 78, 30, this::alJugar));
        columna.add(Box.createVerticalStrut(14));
        columna.add(boton("menu.opciones", Iconos.Tipo.ENGRANAJE, 380, 62, 23, () -> nav.ir(IdPantalla.OPCIONES)));
        columna.add(Box.createVerticalStrut(12));
        columna.add(boton("menu.estadisticas", Iconos.Tipo.BARRAS, 380, 62, 23, () -> nav.ir(IdPantalla.ESTADISTICAS)));
        columna.add(Box.createVerticalStrut(12));
        columna.add(boton("menu.guia", Iconos.Tipo.LIBRO, 380, 62, 23, () -> nav.ir(IdPantalla.GUIA)));
        columna.add(Box.createVerticalStrut(12));
        columna.add(boton("menu.salir", Iconos.Tipo.SALIDA, 380, 62, 23, nav::salir));
        add(centrado(columna, 8), BorderLayout.CENTER);
    }

    private JComponent boton(String clave, Iconos.Tipo icono, int ancho, int alto, float fuente, Runnable accion) {
        BotonMarino boton = new BotonMarino(Textos.t(clave), Iconos.de(icono, (int) (alto * 0.44)),
                BotonMarino.Forma.CHAFLAN, ancho, alto, fuente).conSeparador();
        boton.setAlignmentX(CENTER_ALIGNMENT);
        boton.addActionListener(e -> accion.run());
        return boton;
    }

    /* ventana de partida guardada */
    private void alJugar() {
        DialogoBotones dialogo = new DialogoBotones(Textos.t("guardada.titulo"));
        dialogo.setMensaje(Textos.t("guardada.msg"));
        dialogo.agregarBoton(Textos.t("guardada.continuar"), Iconos.de(Iconos.Tipo.JUGAR, 24, Tema.CIAN), () -> {
            nav.cerrarModal();
            nav.ir(IdPantalla.JUEGO);
        });
        dialogo.agregarBoton(Textos.t("guardada.nueva"), Iconos.de(Iconos.Tipo.PISTOLA, 24, Tema.CIAN), () -> {
            nav.cerrarModal();
            nav.ir(IdPantalla.SELECCION);
        });
        dialogo.agregarBoton(Textos.t("comun.menu"), Iconos.de(Iconos.Tipo.ANCLA, 24, Tema.CIAN), nav::cerrarModal);
        nav.mostrarModal(dialogo, true, null, Tema.VELO_CLARO, null);
    }
}
