package co.edu.uptc.view.pantallas;

import co.edu.uptc.view.IdPantalla;
import co.edu.uptc.view.Navegador;
import co.edu.uptc.view.Tema;
import co.edu.uptc.view.Textos;
import co.edu.uptc.view.componentes.BarraInferior;
import co.edu.uptc.view.componentes.BotonMarino;
import co.edu.uptc.view.componentes.CabeceraPantalla;
import co.edu.uptc.view.componentes.Iconos;
import co.edu.uptc.view.componentes.OpcionIcono;
import co.edu.uptc.view.componentes.PanelTranslucido;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

/* 1 jugador (contra el computador) o 2 jugadores */
public class PantallaSeleccionJugadores extends Pantalla {

    private int cantidad = 1;
    private OpcionIcono uno;
    private OpcionIcono dos;

    public PantallaSeleccionJugadores(Navegador nav) {
        super(nav);
    }

    @Override
    protected void construir() {
        cantidad = 1;
        add(new CabeceraPantalla(Textos.t("sel.titulo")), BorderLayout.NORTH);

        PanelTranslucido panel = new PanelTranslucido(new BorderLayout(0, 18));
        panel.setBorder(BorderFactory.createEmptyBorder(34, 40, 30, 40));
        panel.setPreferredSize(new Dimension(800, 270));
        JLabel pregunta = new JLabel(Textos.t("sel.pregunta"), SwingConstants.CENTER);
        pregunta.setFont(Tema.fuente(Font.BOLD, 21));
        pregunta.setForeground(Tema.TEXTO);
        panel.add(pregunta, BorderLayout.NORTH);

        uno = new OpcionIcono(Textos.t("sel.uno"), Iconos.de(Iconos.Tipo.PERSONA, 64), 190, 130);
        dos = new OpcionIcono(Textos.t("sel.dos"), Iconos.de(Iconos.Tipo.PERSONAS, 64), 190, 130);
        OpcionIcono regresar = new OpcionIcono(Textos.t("sel.regresar"), Iconos.de(Iconos.Tipo.ATRAS, 64), 190, 130);
        uno.addActionListener(e -> seleccionar(1));
        dos.addActionListener(e -> seleccionar(2));
        regresar.addActionListener(e -> nav.ir(IdPantalla.MENU));
        JPanel opciones = new JPanel(new FlowLayout(FlowLayout.CENTER, 36, 0));
        opciones.setOpaque(false);
        opciones.add(uno);
        opciones.add(dos);
        opciones.add(regresar);
        panel.add(opciones, BorderLayout.CENTER);
        seleccionar(1);
        add(centrado(panel, 24), BorderLayout.CENTER);

        BotonMarino jugar = new BotonMarino(Textos.t("comun.jugar"), Iconos.de(Iconos.Tipo.JUGAR, 28),
                BotonMarino.Forma.PILDORA, 300, 58, 22);
        jugar.addActionListener(e -> continuar());
        add(new BarraInferior(null, jugar, null), BorderLayout.SOUTH);
    }

    private void seleccionar(int n) {
        cantidad = n;
        uno.setSeleccionada(n == 1);
        dos.setSeleccionada(n == 2);
    }


    private void continuar() {
        final int jugadores = cantidad;
        nav.editarPerfil(1, () -> {
            if (jugadores == 1) {
                nav.ir(IdPantalla.JUEGO);
            } else {
                nav.editarPerfil(2, () -> nav.ir(IdPantalla.JUEGO));
            }
        });
    }
}
