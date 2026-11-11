package co.edu.uptc.view.componentes;

import co.edu.uptc.view.Tema;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.geom.RoundRectangle2D;

/* Panel de fondo azul */
public class PanelTranslucido extends JPanel {

    private final int arco;

    public PanelTranslucido(LayoutManager layout) {
        this(layout, 28);
    }

    public PanelTranslucido(LayoutManager layout, int arco) {
        super(layout);
        this.arco = arco;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0.create();
        Tema.calidad(g);
        g.setColor(Tema.PANEL);
        g.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), arco, arco));
        g.dispose();
        super.paintComponent(g0);
    }
}
