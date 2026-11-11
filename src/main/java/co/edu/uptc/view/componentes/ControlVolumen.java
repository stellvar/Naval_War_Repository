package co.edu.uptc.view.componentes;

import co.edu.uptc.view.Tema;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.function.IntConsumer;

/* título, icono de altavoz y deslizador  */
public class ControlVolumen extends JPanel {

    private final JSlider deslizador;

    /*
     * alCambiar se llama con cada cambio de valor
     * alSoltar  se llama al soltar el deslizador
     */
    public ControlVolumen(String titulo, int valor, boolean centrado, IntConsumer alCambiar, Runnable alSoltar) {
        super(new BorderLayout(0, 6));
        setOpaque(false);
        JLabel etiqueta = new JLabel(titulo, centrado ? SwingConstants.CENTER : SwingConstants.LEFT);
        etiqueta.setFont(Tema.fuente(Font.BOLD, 17));
        etiqueta.setForeground(Tema.TEXTO);
        add(etiqueta, BorderLayout.NORTH);

        JPanel fila = new JPanel(new BorderLayout(8, 0));
        fila.setOpaque(false);
        fila.add(new JLabel(Iconos.de(Iconos.Tipo.ALTAVOZ, 26, Tema.TEXTO)), BorderLayout.WEST);
        deslizador = new JSlider(0, 100, Math.max(0, Math.min(100, valor)));
        deslizador.setOpaque(false);
        deslizador.setFocusable(false);
        deslizador.setUI(new FinoUI(deslizador));
        deslizador.setPreferredSize(new Dimension(250, 28));
        deslizador.addChangeListener(e -> {
            if (alCambiar != null) {
                alCambiar.accept(deslizador.getValue());
            }
            if (!deslizador.getValueIsAdjusting() && alSoltar != null) {
                alSoltar.run();
            }
        });
        fila.add(deslizador, BorderLayout.CENTER);
        add(fila, BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));
    }

    public int getValor() {
        return deslizador.getValue();
    }

    /* deslizador pintado */
    private static final class FinoUI extends BasicSliderUI {

        FinoUI(JSlider slider) {
            super(slider);
        }

        @Override
        protected Dimension getThumbSize() {
            return new Dimension(16, 16);
        }

        @Override
        public void paintTrack(Graphics g0) {
            Graphics2D g = (Graphics2D) g0.create();
            Tema.calidad(g);
            Rectangle pista = trackRect;
            double y = pista.y + pista.height / 2.0;
            g.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.setColor(new Color(255, 255, 255, 120));
            g.draw(new Line2D.Double(pista.x, y, pista.x + pista.width, y));
            g.setColor(Tema.CIAN);
            g.draw(new Line2D.Double(pista.x, y, thumbRect.x + thumbRect.width / 2.0, y));
            g.dispose();
        }

        @Override
        public void paintThumb(Graphics g0) {
            Graphics2D g = (Graphics2D) g0.create();
            Tema.calidad(g);
            g.setColor(Color.WHITE);
            g.fill(new Ellipse2D.Double(thumbRect.x + 1, thumbRect.y + 1, thumbRect.width - 2, thumbRect.height - 2));
            g.dispose();
        }

        @Override
        public void paintFocus(Graphics g) {
        }
    }
}
