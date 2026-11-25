package co.edu.uptc.view.componentes;

import co.edu.uptc.view.EstadisticasJugadorVista;
import co.edu.uptc.view.Tema;
import co.edu.uptc.view.Textos;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

public class PanelEstadisticasJugador extends TarjetaModal {

    private static final int ANCHO = 360;

    public PanelEstadisticasJugador(EstadisticasJugadorVista datos, Runnable alCerrar) {
        JPanel cabecera = new JPanel(new BorderLayout(16, 0));
        cabecera.setOpaque(false);
        cabecera.setAlignmentX(Component.CENTER_ALIGNMENT);
        cabecera.add(new JLabel(Avatares.icono(datos.avatar(), 72)), BorderLayout.WEST);
        JLabel nombre = new JLabel(datos.nombre());
        nombre.setFont(Tema.fuente(Font.BOLD, 26));
        nombre.setForeground(Tema.CIAN);
        cabecera.add(nombre, BorderLayout.CENTER);
        cabecera.setMaximumSize(new Dimension(ANCHO, 76));
        add(cabecera);

        if (datos.biografia() != null && !datos.biografia().isBlank()) {
            add(Box.createVerticalStrut(12));
            JLabel bio = new JLabel("<html><div style='width:" + ANCHO + "px'>" + escapar(datos.biografia())
                    + "</div></html>");
            bio.setFont(Tema.fuente(Font.PLAIN, 15));
            bio.setForeground(Tema.TEXTO);
            bio.setAlignmentX(Component.CENTER_ALIGNMENT);
            add(bio);
        }
        add(Box.createVerticalStrut(18));
        add(fila(Textos.t("est.jugadas"), String.valueOf(datos.jugadas())));
        add(fila(Textos.t("est.ganadas"), String.valueOf(datos.ganadas())));
        add(fila(Textos.t("est.perdidas"), String.valueOf(datos.perdidas())));
        add(fila(Textos.t("est.mejor"), datos.mejorTurnos() == 0
                ? Textos.t("est.sinVictorias") : String.valueOf(datos.mejorTurnos())));
        add(Box.createVerticalStrut(20));

        BotonMarino cerrar = new BotonMarino(Textos.t("comun.cerrar"), Iconos.de(Iconos.Tipo.ANCLA, 24, Tema.CIAN),
                BotonMarino.Forma.CHAFLAN, 320, 54, 18);
        cerrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        cerrar.addActionListener(e -> alCerrar.run());
        add(cerrar);
    }

    private static JPanel fila(String etiqueta, String valor) {
        JPanel fila = new JPanel(new BorderLayout(12, 0));
        fila.setOpaque(false);
        fila.setAlignmentX(Component.CENTER_ALIGNMENT);
        fila.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 0, 5, 0));
        JLabel izquierda = new JLabel(etiqueta);
        izquierda.setFont(Tema.fuente(Font.BOLD, 17));
        izquierda.setForeground(Tema.TEXTO);
        JLabel derecha = new JLabel(valor, SwingConstants.RIGHT);
        derecha.setFont(Tema.fuente(Font.BOLD, 17));
        derecha.setForeground(Tema.CIAN);
        fila.add(izquierda, BorderLayout.WEST);
        fila.add(derecha, BorderLayout.EAST);
        fila.setPreferredSize(new Dimension(ANCHO, 32));
        fila.setMaximumSize(new Dimension(ANCHO, 32));
        return fila;
    }

    private static String escapar(String texto) {
        return texto.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
