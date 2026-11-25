package co.edu.uptc.view.componentes;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import co.edu.uptc.view.Audio;
import co.edu.uptc.view.ControladorVista;
import co.edu.uptc.view.Tema;
import co.edu.uptc.view.Textos;

/* configuración de sonido: aplica el cambio al audio y lo entrega al controlador para guardarlo */
public class PanelSonido extends JPanel {

    public PanelSonido(boolean centrado, ControladorVista ctrl) {
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        float alineacion = centrado ? Component.CENTER_ALIGNMENT : Component.LEFT_ALIGNMENT;

        JLabel titulo = new JLabel(Textos.t("sonido.titulo"));
        titulo.setFont(Tema.fuente(Font.BOLD, 18));
        titulo.setForeground(Tema.TEXTO);
        titulo.setAlignmentX(alineacion);
        add(titulo);
        add(Box.createVerticalStrut(22));

        ControlVolumen musica = new ControlVolumen(Textos.t("sonido.musica"), ctrl.getVolumenMusica(), centrado,
                valor -> {
                    Audio.setVolumenMusica(valor);
                    ctrl.setVolumenMusica(valor);
                }, ctrl::guardarConfiguracion);
        musica.setAlignmentX(alineacion);
        add(musica);
        add(Box.createVerticalStrut(12));

        ControlVolumen efectos = new ControlVolumen(Textos.t("sonido.efectos"), ctrl.getVolumenEfectos(), centrado,
                valor -> {
                    Audio.setVolumenEfectos(valor);
                    ctrl.setVolumenEfectos(valor);
                }, ctrl::guardarConfiguracion);
        efectos.setAlignmentX(alineacion);
        add(efectos);
        add(Box.createVerticalStrut(14));

        JPanel voz = new JPanel(new BorderLayout(18, 0));
        voz.setOpaque(false);
        JLabel etiquetaVoz = new JLabel(Textos.t("sonido.voz"));
        etiquetaVoz.setFont(Tema.fuente(Font.BOLD, 17));
        etiquetaVoz.setForeground(Tema.TEXTO);
        voz.add(etiquetaVoz, BorderLayout.WEST);
        voz.add(new Interruptor(ctrl.isVozDeMando(), activa -> {
            ctrl.setVozDeMando(activa);
            ctrl.guardarConfiguracion();
        }), BorderLayout.EAST);
        voz.setAlignmentX(alineacion);
        add(voz);
    }
}
