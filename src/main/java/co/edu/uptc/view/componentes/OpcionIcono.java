package co.edu.uptc.view.componentes;

import co.edu.uptc.view.Tema;

import javax.swing.Icon;
import javax.swing.JButton;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

/* icono arriba y texto abajo */
public class OpcionIcono extends JButton {

    private final Icon icono;
    private boolean seleccionada;

    public OpcionIcono(String texto, Icon icono, int ancho, int alto) {
        super(texto);
        this.icono = icono;
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setRolloverEnabled(true);
        setFont(Tema.fuente(Font.BOLD, 17));
        setForeground(Tema.TEXTO);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        Dimension tam = new Dimension(ancho, alto);
        setPreferredSize(tam);
        setMinimumSize(tam);
        setMaximumSize(tam);
    }

    public void setSeleccionada(boolean seleccionada) {
        this.seleccionada = seleccionada;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0.create();
        Tema.calidad(g);
        int w = getWidth();
        int h = getHeight();
        RoundRectangle2D forma = new RoundRectangle2D.Double(1, 1, w - 2, h - 2, 22, 22);
        if (seleccionada) {
            g.setColor(new Color(162, 231, 255, 45));
            g.fill(forma);
            g.setColor(Tema.CIAN);
            g.setStroke(new BasicStroke(2f));
            g.draw(forma);
        } else if (getModel().isRollover()) {
            g.setColor(new Color(255, 255, 255, 28));
            g.fill(forma);
        }
        FontMetrics fm = g.getFontMetrics(getFont());
        int altoTotal = icono.getIconHeight() + 10 + fm.getHeight();
        int y0 = (h - altoTotal) / 2;
        icono.paintIcon(this, g, (w - icono.getIconWidth()) / 2, y0);
        g.setFont(getFont());
        g.setColor(seleccionada ? Tema.CIAN : getForeground());
        g.drawString(getText(), (w - fm.stringWidth(getText())) / 2, y0 + icono.getIconHeight() + 10 + fm.getAscent());
        g.dispose();
    }
}
