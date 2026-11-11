package co.edu.uptc.view.componentes;

import co.edu.uptc.view.Tema;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;

/* Una fila de la tabla de estadísticas: avatar, nombre, línea guía y turnos */
public class FilaRanking extends JPanel {

    private static final int ANCHO_NOMBRE = 250;

    private FilaRanking() {
        super(new GridBagLayout());
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(6, 0, 6, 0));
    }

    /* encabezado */
    public static FilaRanking encabezado(String columnaNombre, String columnaValor) {
        FilaRanking fila = new FilaRanking();
        fila.agregar(new JLabel(), 0);
        fila.agregar(texto(columnaNombre, Tema.CIAN, SwingConstants.LEFT), 1);
        fila.agregar(new Guia(), 2);
        fila.agregar(texto(columnaValor, Tema.CIAN, SwingConstants.RIGHT), 3);
        return fila;
    }

    public static FilaRanking de(String nombre, int avatar, int turnos) {
        FilaRanking fila = new FilaRanking();
        fila.agregar(new JLabel(Avatares.icono(avatar, 34)), 0);
        fila.agregar(texto(nombre, Tema.TEXTO, SwingConstants.LEFT), 1);
        fila.agregar(new Guia(), 2);
        fila.agregar(texto(String.valueOf(turnos), Tema.TEXTO, SwingConstants.RIGHT), 3);
        return fila;
    }

    private static JLabel texto(String valor, Color color, int alineacion) {
        JLabel etiqueta = new JLabel(valor, alineacion);
        etiqueta.setFont(Tema.fuente(Font.BOLD, 17));
        etiqueta.setForeground(color);
        return etiqueta;
    }

    private void agregar(JComponent componente, int columna) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = columna;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, columna == 0 ? 0 : 10, 0, 0);
        if (columna == 0) {
            componente.setPreferredSize(new Dimension(34, 34));
        } else if (columna == 1) {
            componente.setPreferredSize(new Dimension(ANCHO_NOMBRE, 30));
        } else if (columna == 2) {
            c.weightx = 1;
        } else {
            componente.setPreferredSize(new Dimension(80, 30));
        }
        add(componente, c);
    }

    /* line aguia */
    private static final class Guia extends JComponent {
        @Override
        protected void paintComponent(Graphics g0) {
            Graphics2D g = (Graphics2D) g0.create();
            g.setColor(new Color(255, 255, 255, 110));
            int y = getHeight() / 2;
            g.drawLine(0, y, getWidth(), y);
            g.dispose();
        }
    }
}
