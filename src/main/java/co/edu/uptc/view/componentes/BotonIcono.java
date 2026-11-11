package co.edu.uptc.view.componentes;

import co.edu.uptc.view.Tema;

import javax.swing.Icon;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;


public class BotonIcono extends JButton {

    private final Icon icono;
    private boolean activo;

    public BotonIcono(Icon icono, int diametro) {
        this.icono = icono;
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setRolloverEnabled(true);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(diametro, diametro));
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0.create();
        Tema.calidad(g);
        int w = getWidth();
        int h = getHeight();
        if (activo) {
            g.setColor(new Color(4, 30, 58, 245));
            g.fillOval(0, 0, w, h);
        } else if (getModel().isRollover()) {
            g.setColor(new Color(255, 255, 255, 40));
            g.fillOval(0, 0, w, h);
        }
        icono.paintIcon(this, g, (w - icono.getIconWidth()) / 2, (h - icono.getIconHeight()) / 2);
        g.dispose();
    }
}
