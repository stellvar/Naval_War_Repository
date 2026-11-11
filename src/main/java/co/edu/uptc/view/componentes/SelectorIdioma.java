package co.edu.uptc.view.componentes;

import co.edu.uptc.view.Tema;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.util.function.Consumer;


public class SelectorIdioma extends JComponent {

    private record Opcion(String codigo, String nombre, Icon bandera) {
    }

    private final Opcion[] opciones = {
            new Opcion("es", "ESPAÑOL", Banderas.espana(22)),
            new Opcion("en", "ENGLISH", Banderas.reinoUnido(22))
    };
    private Opcion actual;

    public SelectorIdioma(String codigoActual, Consumer<String> alCambiar) {
        actual = opciones[0];
        for (Opcion o : opciones) {
            if (o.codigo().equals(codigoActual)) {
                actual = o;
            }
        }
        setPreferredSize(new Dimension(220, 34));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                JPopupMenu menu = new JPopupMenu();
                menu.setBorder(BorderFactory.createLineBorder(Tema.BOTON_BORDE));
                menu.setBackground(Tema.BOTON);
                for (Opcion o : opciones) {
                    JMenuItem item = new JMenuItem(o.nombre(), o.bandera());
                    item.setFont(Tema.fuente(Font.BOLD, 15));
                    item.setForeground(Tema.CIAN);
                    item.setBackground(Tema.BOTON);
                    item.addActionListener(ev -> {
                        actual = o;
                        repaint();
                        alCambiar.accept(o.codigo());
                    });
                    menu.add(item);
                }
                menu.show(SelectorIdioma.this, 0, getHeight());
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g0) {
        Graphics2D g = (Graphics2D) g0.create();
        Tema.calidad(g);
        int w = getWidth();
        int h = getHeight();
        g.setColor(Tema.CAMPO);
        g.fillRoundRect(0, 0, w, h, 14, 14);
        actual.bandera().paintIcon(this, g, 10, (h - actual.bandera().getIconHeight()) / 2);
        g.setFont(Tema.fuente(Font.BOLD, 16));
        g.setColor(Tema.CAMPO_TEXTO);
        FontMetrics fm = g.getFontMetrics();
        g.drawString(actual.nombre(), 42, (h - fm.getHeight()) / 2 + fm.getAscent());
        Path2D flecha = new Path2D.Double();
        flecha.moveTo(w - 26, h / 2.0 - 3);
        flecha.lineTo(w - 19, h / 2.0 + 4);
        flecha.lineTo(w - 12, h / 2.0 - 3);
        g.setStroke(new java.awt.BasicStroke(2f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND));
        g.draw(flecha);
        g.dispose();
    }
}
