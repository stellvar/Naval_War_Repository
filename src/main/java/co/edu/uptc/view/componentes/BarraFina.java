package co.edu.uptc.view.componentes;

import co.edu.uptc.view.Tema;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class BarraFina extends BasicScrollBarUI {

    @Override
    protected void configureScrollBarColors() {
        thumbColor = new Color(255, 255, 255, 110);
        trackColor = new Color(0, 0, 0, 0);
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return cero();
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return cero();
    }

    private static JButton cero() {
        JButton boton = new JButton();
        boton.setPreferredSize(new Dimension(0, 0));
        return boton;
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle bounds) {
    }

    @Override
    protected void paintThumb(Graphics g0, JComponent c, Rectangle r) {
        Graphics2D g = (Graphics2D) g0.create();
        Tema.calidad(g);
        g.setColor(thumbColor);
        g.fillRoundRect(r.x + 1, r.y, r.width - 2, r.height, 8, 8);
        g.dispose();
    }

    @Override
    public Dimension getPreferredSize(JComponent c) {
        return new Dimension(10, 10);
    }
}
