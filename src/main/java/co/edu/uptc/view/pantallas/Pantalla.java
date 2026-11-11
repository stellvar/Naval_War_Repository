package co.edu.uptc.view.pantallas;

import co.edu.uptc.view.Navegador;
import co.edu.uptc.view.componentes.PanelFondo;

import javax.swing.*;
import java.awt.*;


public abstract class Pantalla extends PanelFondo {

    protected final Navegador nav;

    protected Pantalla(Navegador nav) {
        this.nav = nav;
        setLayout(new BorderLayout());
    }


    protected abstract void construir();

    public void alMostrar() {
        removeAll();
        construir();
        revalidate();
        repaint();
    }

    protected static JPanel centrado(JComponent componente, int arriba) {
        JPanel envoltorio = new JPanel(new GridBagLayout());
        envoltorio.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTH;
        c.weighty = 1;
        c.insets = new Insets(arriba, 0, 0, 0);
        envoltorio.add(componente, c);
        return envoltorio;
    }

    protected static JPanel expandido(JComponent componente, Insets margenes) {
        JPanel envoltorio = new JPanel(new GridBagLayout());
        envoltorio.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        c.insets = margenes;
        envoltorio.add(componente, c);
        return envoltorio;
    }
}
