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
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;


public class BotonMarino extends JButton {

    public enum Forma { CHAFLAN, PILDORA }

    private final Forma forma;
    private final Icon icono;
    private boolean separador;

    public BotonMarino(String texto, Icon icono, Forma forma, int ancho, int alto, float tamFuente) {
        super(texto);
        this.icono = icono;
        this.forma = forma;
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setRolloverEnabled(true);
        setForeground(Tema.CIAN);
        setFont(Tema.fuente(Font.BOLD, tamFuente));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        Dimension tam = new Dimension(ancho, alto);
        setPreferredSize(tam);
        setMinimumSize(tam);
        setMaximumSize(tam);
    }

    public BotonMarino conSeparador() {
        this.separador = true;
        return this;
    }

    private Shape forma(double w, double h) {
        if (forma == Forma.PILDORA) {
            return new RoundRectangle2D.Double(1, 1, w - 2, h - 2, h - 2, h - 2);
        }
        double c = h * 0.30;
        Path2D p = new Path2D.Double();
        p.moveTo(c, 1);
        p.lineTo(w - c, 1);
        p.lineTo(w - 1, c);
        p.lineTo(w - 1, h - c);
        p.lineTo(w - c, h - 1);
        p.lineTo(c, h - 1);
        p.lineTo(1, h - c);
        p.lineTo(1, c);
        p.closePath();
        return p;
    }

    @Override
    protected void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0.create();
        Tema.calidad(g);
        int w = getWidth();
        int h = getHeight();
        Shape contorno = forma(w, h);

        Color relleno = Tema.BOTON;
        if (!isEnabled()) {
            relleno = new Color(4, 30, 58, 120);
        } else if (getModel().isPressed()) {
            relleno = Tema.BOTON_PRESIONADO;
        } else if (getModel().isRollover()) {
            relleno = Tema.BOTON_HOVER;
        }
        g.setColor(relleno);
        g.fill(contorno);
        g.setColor(isEnabled() ? Tema.BOTON_BORDE : new Color(27, 79, 133, 90));
        g.setStroke(new BasicStroke(1.5f));
        g.draw(contorno);

        Color tinta = isEnabled() ? getForeground() : new Color(162, 231, 255, 110);
        int inicioTexto = 0;
        if (icono != null) {
            int x = (int) Math.round(h * 0.42);
            icono.paintIcon(this, g, x, (h - icono.getIconHeight()) / 2);
            inicioTexto = x + icono.getIconWidth() + 10;
            if (separador) {
                int xs = inicioTexto + 6;
                g.setColor(tinta);
                g.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g.draw(new Line2D.Double(xs, h * 0.28, xs, h * 0.72));
                inicioTexto = xs + 6;
            }
        }
        g.setFont(getFont());
        FontMetrics fm = g.getFontMetrics();
        int zona = w - inicioTexto - (int) (h * 0.30);
        int tx = inicioTexto + (zona - fm.stringWidth(getText())) / 2;
        int ty = (h - fm.getHeight()) / 2 + fm.getAscent();
        g.setColor(tinta);
        g.drawString(getText(), tx, ty);
        g.dispose();
    }
}
