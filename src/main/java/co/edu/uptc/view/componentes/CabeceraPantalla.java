package co.edu.uptc.view.componentes;

import co.edu.uptc.view.Tema;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;


public class CabeceraPantalla extends JPanel {

    private static final int DIAMETRO = 92;
    private final JLabel titulo;

    public CabeceraPantalla(String texto) {
        setOpaque(false);
        setLayout(new BorderLayout());
        titulo = new JLabel(texto, SwingConstants.CENTER);
        titulo.setFont(Tema.fuente(java.awt.Font.BOLD, 36));
        titulo.setForeground(Tema.CIAN);
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        add(titulo, BorderLayout.SOUTH);
        setPreferredSize(new Dimension(100, 166));
    }

    public void setTitulo(String texto) {
        titulo.setText(texto);
    }

    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0.create();
        Tema.calidad(g);
        int w = getWidth();
        int cx = w / 2;
        int cy = 12 + DIAMETRO / 2;

        double largo = Math.min(w * 0.22, 320);
        int separacion = 26;
        g.setColor(new Color(255, 255, 255, 215));
        g.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        double xi = cx - DIAMETRO / 2.0 - separacion;
        g.draw(new Line2D.Double(xi - largo, cy, xi, cy));
        g.draw(new Line2D.Double(xi - largo, cy, xi - largo + 8, cy - 4));
        g.draw(new Line2D.Double(xi - largo, cy, xi - largo + 8, cy + 4));
        double xd = cx + DIAMETRO / 2.0 + separacion;
        g.draw(new Line2D.Double(xd, cy, xd + largo, cy));
        g.draw(new Line2D.Double(xd + largo, cy, xd + largo - 8, cy - 4));
        g.draw(new Line2D.Double(xd + largo, cy, xd + largo - 8, cy + 4));

        g.setColor(new Color(4, 30, 58, 190));
        g.fillOval(cx - DIAMETRO / 2, cy - DIAMETRO / 2, DIAMETRO, DIAMETRO);
        g.setColor(Color.WHITE);
        g.setStroke(new BasicStroke(3.2f));
        g.drawOval(cx - DIAMETRO / 2, cy - DIAMETRO / 2, DIAMETRO, DIAMETRO);
        Iconos.de(Iconos.Tipo.ANCLA, 58, Color.WHITE).paintIcon(this, g, cx - 29, cy - 29);
        g.dispose();
    }
}
