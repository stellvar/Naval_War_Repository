package co.edu.uptc.view.componentes;

import co.edu.uptc.view.Tema;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

/* velo que deja huecos sobre los componentes seleccionables
 */
public class SuperposicionModal extends JPanel {

    private Color velo = Tema.VELO_CLARO;
    private Component huecoDe;
    private boolean cerrarAlClicFuera;
    private Runnable alCerrar;
    private JComponent contenido;

    public SuperposicionModal() {
        super(new GridBagLayout());
        setOpaque(false);
        setVisible(false);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (cerrarAlClicFuera && (contenido == null || !contenido.getBounds().contains(e.getPoint()))) {
                    cerrar();
                }
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
        });
    }

    public void mostrar(JComponent contenido, boolean cerrarAlClicFuera, Component huecoDe, Color velo, Runnable alCerrar) {
        cerrar();
        this.contenido = contenido;
        this.cerrarAlClicFuera = cerrarAlClicFuera;
        this.huecoDe = huecoDe;
        this.velo = velo == null ? Tema.VELO_CLARO : velo;
        this.alCerrar = alCerrar;
        add(contenido, new GridBagConstraints());
        setVisible(true);
        revalidate();
        repaint();
    }

    public void cerrar() {
        if (!isVisible()) {
            return;
        }
        removeAll();
        setVisible(false);
        contenido = null;
        huecoDe = null;
        Runnable pendiente = alCerrar;
        alCerrar = null;
        if (pendiente != null) {
            pendiente.run();
        }
    }

    public boolean estaAbierta() {
        return isVisible();
    }

    private Shape hueco() {
        if (huecoDe == null || !huecoDe.isShowing()) {
            return null;
        }
        Rectangle r = SwingUtilities.convertRectangle(huecoDe.getParent(), huecoDe.getBounds(), this);
        return new Ellipse2D.Double(r.x - 3, r.y - 3, r.width + 6, r.height + 6);
    }

    @Override
    public boolean contains(int x, int y) {
        Shape hueco = hueco();
        if (hueco != null && hueco.contains(x, y)) {
            return false;
        }
        return super.contains(x, y);
    }

    @Override
    protected void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0.create();
        Tema.calidad(g);
        Area area = new Area(new Rectangle(0, 0, getWidth(), getHeight()));
        Shape hueco = hueco();
        if (hueco != null) {
            area.subtract(new Area(hueco));
        }
        g.setColor(velo);
        g.fill(area);
        g.dispose();
    }
}
