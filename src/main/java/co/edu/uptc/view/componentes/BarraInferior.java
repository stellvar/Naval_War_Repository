package co.edu.uptc.view.componentes;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

public class BarraInferior extends JPanel {

    public BarraInferior(Component izquierda, Component centro, Component derecha) {
        super(new BorderLayout());
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(0, 52, 30, 52));
        setPreferredSize(new Dimension(100, 96));
        if (izquierda != null) {
            add(izquierda, BorderLayout.WEST);
        }
        if (derecha != null) {
            add(derecha, BorderLayout.EAST);
        }
        if (centro != null) {
            if (centro instanceof JButton) {
                JPanel envoltorio = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
                envoltorio.setOpaque(false);
                envoltorio.add(centro);
                add(envoltorio, BorderLayout.CENTER);
            } else {
                add(centro, BorderLayout.CENTER);
            }
        }
    }
}
