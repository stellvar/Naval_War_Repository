package co.edu.uptc.view.componentes;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import co.edu.uptc.view.Tema;

/* tarjeta oscura redondeada  */
public class TarjetaModal extends JPanel {

    public TarjetaModal() {
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(26, 40, 28, 40));
    }

    @Override
    protected void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0.create();
        Tema.calidad(g);
        RoundRectangle2D forma = new RoundRectangle2D.Double(1, 1, getWidth() - 2, getHeight() - 2, 28, 28);
        g.setColor(Tema.PANEL_MODAL);
        g.fill(forma);
        g.setColor(Tema.BOTON_BORDE);
        g.setStroke(new BasicStroke(1.6f));
        g.draw(forma);
        g.dispose();
        super.paintComponent(g0);
    }
}

