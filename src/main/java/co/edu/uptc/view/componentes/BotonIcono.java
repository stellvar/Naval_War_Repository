package co.edu.uptc.view.componentes;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.Icon;
import javax.swing.JButton;

import co.edu.uptc.view.Tema;


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
        Color fondo = Tema.CIRCULO;
        if (activo) {
            fondo = Tema.CIRCULO_ACTIVO;
        } else if (getModel().isRollover()) {
            fondo = Tema.CIRCULO_HOVER;
        }
        int d = Math.min(w, h);
        g.setColor(fondo);
        g.fillOval((w - d) / 2, (h - d) / 2, d, d);
        icono.paintIcon(this, g, (w - icono.getIconWidth()) / 2, (h - icono.getIconHeight()) / 2);
        g.dispose();
    }
}
