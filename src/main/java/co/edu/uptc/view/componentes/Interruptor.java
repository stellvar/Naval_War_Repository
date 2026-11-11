package co.edu.uptc.view.componentes;

import co.edu.uptc.view.Tema;

import javax.swing.JComponent;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.util.function.Consumer;

/* Interruptor de encendido / apagado. */
public class Interruptor extends JComponent {

    private boolean activo;
    private final Consumer<Boolean> alCambiar;

    public Interruptor(boolean activo, Consumer<Boolean> alCambiar) {
        this.activo = activo;
        this.alCambiar = alCambiar;
        setPreferredSize(new Dimension(50, 26));
        setMinimumSize(new Dimension(50, 26));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (contains(e.getPoint())) {
                    setActivo(!Interruptor.this.activo);
                    if (Interruptor.this.alCambiar != null) {
                        Interruptor.this.alCambiar.accept(Interruptor.this.activo);
                    }
                }
            }
        });
    }

    public boolean isActivo() {
        return activo;
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
        RoundRectangle2D pista = new RoundRectangle2D.Double(1.5, 1.5, w - 3, h - 3, h - 3, h - 3);
        g.setColor(activo ? new Color(162, 231, 255, 200) : new Color(4, 30, 58, 200));
        g.fill(pista);
        g.setColor(Color.WHITE);
        g.setStroke(new BasicStroke(1.8f));
        g.draw(pista);
        double d = h - 9;
        double x = activo ? w - d - 6 : 6;
        g.setColor(activo ? Tema.BOTON : Color.WHITE);
        g.fill(new Ellipse2D.Double(x, 4.5, d, d));
        g.dispose();
    }
}
