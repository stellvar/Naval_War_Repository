package co.edu.uptc.view.componentes;

import co.edu.uptc.view.Tema;

import javax.swing.Icon;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;


public final class Banderas {

    private Banderas() {
    }

    public static Icon espana(int ancho) {
        return nueva(ancho, (g, w, h) -> {
            g.setColor(new Color(0xC60B1E));
            g.fill(new Rectangle2D.Double(0, 0, w, h));
            g.setColor(new Color(0xFFC400));
            g.fill(new Rectangle2D.Double(0, h / 4, w, h / 2));
        });
    }

    public static Icon reinoUnido(int ancho) {
        return nueva(ancho, (g, w, h) -> {
            g.setColor(new Color(0x012169));
            g.fill(new Rectangle2D.Double(0, 0, w, h));
            g.setColor(Color.WHITE);
            g.setStroke(new BasicStroke((float) (h * 0.2)));
            g.draw(new Line2D.Double(0, 0, w, h));
            g.draw(new Line2D.Double(0, h, w, 0));
            g.setColor(new Color(0xC8102E));
            g.setStroke(new BasicStroke((float) (h * 0.08)));
            g.draw(new Line2D.Double(0, 0, w, h));
            g.draw(new Line2D.Double(0, h, w, 0));
            g.setColor(Color.WHITE);
            g.fill(new Rectangle2D.Double(0, h * 0.35, w, h * 0.30));
            g.fill(new Rectangle2D.Double(w * 0.43, 0, w * 0.14, h));
            g.setColor(new Color(0xC8102E));
            g.fill(new Rectangle2D.Double(0, h * 0.42, w, h * 0.16));
            g.fill(new Rectangle2D.Double(w * 0.465, 0, w * 0.07, h));
        });
    }

    private interface Pintor {
        void pintar(Graphics2D g, double ancho, double alto);
    }

    private static Icon nueva(int ancho, Pintor pintor) {
        final int alto = Math.round(ancho * 2f / 3f);
        return new Icon() {
            @Override
            public int getIconWidth() {
                return ancho;
            }

            @Override
            public int getIconHeight() {
                return alto;
            }

            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                Tema.calidad(g2);
                g2.translate(x, y);
                Shape forma = new Rectangle2D.Double(0, 0, ancho, alto);
                g2.clip(forma);
                pintor.pintar(g2, ancho, alto);
                g2.setClip(null);
                g2.setColor(new Color(0, 0, 0, 80));
                g2.setStroke(new BasicStroke(1f));
                g2.draw(new Rectangle2D.Double(0.5, 0.5, ancho - 1, alto - 1));
                g2.dispose();
            }
        };
    }
}
