package co.edu.uptc.view.componentes;

import co.edu.uptc.view.Audio;
import co.edu.uptc.view.Tema;
import co.edu.uptc.view.Textos;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;

/* de momento no nada ni se pueden cambiar los valores*/
public class PanelSonido extends JPanel {

    public PanelSonido(boolean centrado) {
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        float alineacion = centrado ? Component.CENTER_ALIGNMENT : Component.LEFT_ALIGNMENT;

        JLabel titulo = new JLabel(Textos.t("sonido.titulo"));
        titulo.setFont(Tema.fuente(Font.BOLD, 18));
        titulo.setForeground(Tema.TEXTO);
        titulo.setAlignmentX(alineacion);
        add(titulo);
        add(Box.createVerticalStrut(22));

        ControlVolumen musica = new ControlVolumen(Textos.t("sonido.musica"), 50, centrado, valor -> {
            new ControlVolumen(Textos.t("sonido.musica"), 50, centrado, Audio::setVolumenMusica, () -> { });}
                , () -> { });
        musica.setAlignmentX(alineacion);
        add(musica);
        add(Box.createVerticalStrut(12));


        ControlVolumen efectos = new ControlVolumen(Textos.t("sonido.efectos"), 50, centrado, valor -> {
            new ControlVolumen(Textos.t("sonido.efectos"), 50, centrado, Audio::setVolumenEfectos, () -> { });
        }, () -> { });
        efectos.setAlignmentX(alineacion);
        add(efectos);
        add(Box.createVerticalStrut(14));

        JPanel voz = new JPanel(new BorderLayout(18, 0));
        voz.setOpaque(false);
        JLabel etiquetaVoz = new JLabel(Textos.t("sonido.voz"));
        etiquetaVoz.setFont(Tema.fuente(Font.BOLD, 17));
        etiquetaVoz.setForeground(Tema.TEXTO);
        voz.add(etiquetaVoz, BorderLayout.WEST);
        voz.add(new Interruptor(false, activa -> { }), BorderLayout.EAST);
        voz.setAlignmentX(alineacion);
        add(voz);
    }
}
