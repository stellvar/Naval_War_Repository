package co.edu.uptc.view.componentes;

import co.edu.uptc.view.Tema;
import co.edu.uptc.view.Textos;

import javax.swing.JComponent;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

/* seccion lateral del estado de la flota */
public class PanelFlota extends JComponent {

    private final EstiloFlota estilo;
    private TableroVista tablero;

    public PanelFlota(EstiloFlota estilo) {
        this.estilo = estilo;
        setOpaque(false);
        setPreferredSize(new Dimension(148, 300));
        setMinimumSize(new Dimension(124, 200));
    }

    public void setTablero(TableroVista tablero) {
        this.tablero = tablero;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0.create();
        Tema.calidad(g);
        int w = getWidth();
        int h = getHeight();

        RoundRectangle2D marco = new RoundRectangle2D.Double(1.5, 1.5, w - 3, h - 3, 18, 18);
        g.setColor(estilo.fondo);
        g.fill(marco);
        double cabecera = Math.min(66, h * 0.2);
        Shape previo = g.getClip();
        g.clip(marco);
        g.setColor(estilo.borde);
        g.fill(new Rectangle2D.Double(0, 0, w, cabecera));
        g.setClip(previo);
        g.setColor(estilo.borde);
        g.setStroke(new BasicStroke(3f));
        g.draw(marco);

        TableroVista.Nave[] barcos = tablero == null ? new TableroVista.Nave[0] : tablero.getNaves();
        int restantes = tablero == null ? 0 : tablero.navesRestantes();

        g.setColor(Tema.TEXTO);
        g.setFont(Tema.fuente(Font.BOLD, 14));
        FontMetrics fm = g.getFontMetrics();
        String titulo = Textos.t("juego.flota");
        String conteo = restantes + "/" + Math.max(barcos.length, 4);
        double bloque = fm.getHeight() * 2;
        double y0 = (cabecera - bloque) / 2 + fm.getAscent();
        g.drawString(titulo, (w - fm.stringWidth(titulo)) / 2f, (float) y0);
        g.drawString(conteo, (w - fm.stringWidth(conteo)) / 2f, (float) (y0 + fm.getHeight()));

        if (barcos.length == 0) {
            g.dispose();
            return;
        }
        double zona = (h - cabecera - 10) / barcos.length;
        g.setFont(Tema.fuente(Font.BOLD, 13));
        fm = g.getFontMetrics();
        for (int i = 0; i < barcos.length; i++) {
            TableroVista.Nave barco = barcos[i];
            double yBase = cabecera + 10 + i * zona;
            g.setColor(Tema.TEXTO);
            g.drawString(Textos.t("barco." + barco.getTamano()), 14, (float) (yBase + fm.getAscent() + 2));
            double segmento = Math.min(21, (w - 28.0) / 5);
            double yBarra = yBase + fm.getHeight() + 8;
            boolean[] impactos = barco.getImpactos();
            for (int s = 0; s < barco.getTamano(); s++) {
                double x = 14 + s * (segmento + 2);
                Rectangle2D caja = new Rectangle2D.Double(x, yBarra, segmento, 13);
                g.setColor(new Color(0xF3F7FB));
                g.fill(caja);
                g.setColor(new Color(0x07142E));
                g.setStroke(new BasicStroke(1.4f));
                g.draw(caja);
                if (impactos[s]) {
                    g.setColor(Tema.IMPACTO);
                    g.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                    g.draw(new Line2D.Double(x + 4, yBarra + 3, x + segmento - 4, yBarra + 10));
                    g.draw(new Line2D.Double(x + 4, yBarra + 10, x + segmento - 4, yBarra + 3));
                }
            }
        }
        g.dispose();
    }
}
