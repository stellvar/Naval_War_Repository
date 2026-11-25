package co.edu.uptc.view.componentes;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

public class BarraInferior extends JPanel {

    public BarraInferior(Component izquierda, Component centro, Component derecha) {
        super(new BorderLayout());
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(0, 52, 30, 52));
        setPreferredSize(new Dimension(100, 96));
        if (izquierda != null) {
            add(envolver(izquierda, GridBagConstraints.WEST), BorderLayout.WEST);
        }
        if (derecha != null) {
            add(envolver(derecha, GridBagConstraints.EAST), BorderLayout.EAST);
        }
        if (centro != null) {
            if (centro instanceof JButton) {
                add(envolver(centro, GridBagConstraints.CENTER), BorderLayout.CENTER);
            } else {
                add(centro, BorderLayout.CENTER);
            }
        }
    }


    private static JPanel envolver(Component componente, int ancla) {
        JPanel envoltorio = new JPanel(new GridBagLayout());
        envoltorio.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1;
        c.anchor = ancla;
        envoltorio.add(componente, c);
        return envoltorio;
    }
}
