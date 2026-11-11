package co.edu.uptc.view.componentes;

import co.edu.uptc.view.Tema;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import java.awt.BasicStroke;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;

/* cuadrícula de avatares seleccionables. */
public class SelectorAvatar extends JPanel {

    private final JToggleButton[] botones = new JToggleButton[Avatares.CANTIDAD];
    private int seleccionado = Avatares.POR_DEFECTO;

    public SelectorAvatar(int tamano) {
        super(new GridLayout(2, 3, 20, 20));
        setOpaque(false);
        ButtonGroup grupo = new ButtonGroup();
        for (int i = 0; i < botones.length; i++) {
            final int indice = i;
            JToggleButton boton = new JToggleButton() {
                @Override
                protected void paintComponent(Graphics g0) {
                    Graphics2D g = (Graphics2D) g0.create();
                    Tema.calidad(g);
                    int w = getWidth();
                    int h = getHeight();
                    Avatares.dibujar(g, indice, 5, 5, w - 10);
                    if (isSelected()) {
                        g.setColor(Tema.CIAN);
                        g.setStroke(new BasicStroke(3.2f));
                        g.draw(new RoundRectangle2D.Double(2, 2, w - 4, h - 4, 14, 14));
                    } else if (getModel().isRollover()) {
                        g.setColor(new java.awt.Color(255, 255, 255, 120));
                        g.setStroke(new BasicStroke(2f));
                        g.draw(new RoundRectangle2D.Double(2, 2, w - 4, h - 4, 14, 14));
                    }
                    g.dispose();
                }
            };
            boton.setContentAreaFilled(false);
            boton.setBorderPainted(false);
            boton.setFocusPainted(false);
            boton.setOpaque(false);
            boton.setRolloverEnabled(true);
            boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            boton.setPreferredSize(new Dimension(tamano, tamano));
            boton.addActionListener(e -> seleccionado = indice);
            grupo.add(boton);
            botones[i] = boton;
            add(boton);
        }
        setSeleccionado(seleccionado);
    }

    public int getSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(int indice) {
        if (indice >= 0 && indice < botones.length) {
            seleccionado = indice;
            botones[indice].setSelected(true);
        }
    }
}
