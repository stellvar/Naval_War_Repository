package co.edu.uptc.view.componentes;

import co.edu.uptc.view.Tema;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

/* ("TU FLOTA" / "CAMPO ENEMIGO"). */
public class EtiquetaTitulo extends JLabel {

    private final EstiloFlota estilo;

    public EtiquetaTitulo(String texto, EstiloFlota estilo) {
        super(texto, SwingConstants.CENTER);
        this.estilo = estilo;
        setOpaque(false);
        setFont(Tema.fuente(Font.BOLD, 22));
        setForeground(Tema.TEXTO);
        setPreferredSize(new Dimension(100, 46));
    }

    @Override
    protected void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0.create();
        Tema.calidad(g);
        RoundRectangle2D forma = new RoundRectangle2D.Double(1.5, 1.5, getWidth() - 3, getHeight() - 3, 16, 16);
        g.setColor(estilo.fondo);
        g.fill(forma);
        g.setColor(estilo.borde);
        g.setStroke(new BasicStroke(3f));
        g.draw(forma);
        g.dispose();
        super.paintComponent(g0);
    }
}
