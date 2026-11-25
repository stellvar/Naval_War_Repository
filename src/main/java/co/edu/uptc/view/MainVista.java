package co.edu.uptc.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;


public final class MainVista {

    private MainVista() {
    }

    public static void iniciar(ControladorVista controlador) {
        SwingUtilities.invokeLater(() -> {
            Textos.setIdioma(controlador.getIdioma());
            Audio.setVolumenMusica(controlador.getVolumenMusica());
            Audio.setVolumenEfectos(controlador.getVolumenEfectos());
            Audio.setVozDeMando(controlador.isVozDeMando());

            Toolkit.getDefaultToolkit().addAWTEventListener(ev -> {
                MouseEvent m = (MouseEvent) ev;
                if (m.getID() == MouseEvent.MOUSE_PRESSED
                        && m.getSource() instanceof AbstractButton b && b.isEnabled()) {
                    Audio.efectoClic();
                }
            }, AWTEvent.MOUSE_EVENT_MASK);

            new VentanaPrincipal(controlador).setVisible(true);
            Audio.iniciarMusica();
        });
    }
}
