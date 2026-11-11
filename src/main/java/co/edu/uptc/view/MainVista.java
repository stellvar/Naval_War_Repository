package co.edu.uptc.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;



public final class MainVista {

    private MainVista() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Toolkit.getDefaultToolkit().addAWTEventListener(ev -> {
                MouseEvent m = (MouseEvent) ev;
                if (m.getID() == MouseEvent.MOUSE_PRESSED
                        && m.getSource() instanceof AbstractButton b && b.isEnabled()) {
                    Audio.efectoClic();
                }
            }, AWTEvent.MOUSE_EVENT_MASK);

            new VentanaPrincipal().setVisible(true);
            Audio.iniciarMusica();
        });
    }
}
