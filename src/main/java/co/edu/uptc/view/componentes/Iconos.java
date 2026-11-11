package co.edu.uptc.view.componentes;

import co.edu.uptc.view.Tema;

import javax.swing.Icon;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;

/* iconos en java 2d */
public final class Iconos {

    public enum Tipo { ANCLA, JUGAR, ENGRANAJE, LIBRO, SALIDA, MARCADOR, PISTOLA, PERSONA, PERSONAS, ATRAS, ALTAVOZ, BARRAS }

    private Iconos() {
    }

    public static Icon de(Tipo tipo, int tam) {
        return de(tipo, tam, Tema.TEXTO);
    }

    public static Icon de(Tipo tipo, int tam, Color color) {
        final float grosor = tipo == Tipo.ANCLA ? 1.9f : 1.7f;
        return new Icon() {
            @Override
            public int getIconWidth() {
                return tam;
            }

            @Override
            public int getIconHeight() {
                return tam;
            }

            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                Tema.calidad(g2);
                g2.translate(x, y);
                double escala = tam / 24.0;
                g2.scale(escala, escala);
                g2.setColor(color);
                g2.setStroke(new BasicStroke(grosor, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                dibujar(tipo, g2);
                g2.dispose();
            }
        };
    }

    /* cuadricula de trazos */
    private static void dibujar(Tipo tipo, Graphics2D g) {
        switch (tipo) {
            case ANCLA -> {
                g.draw(new Ellipse2D.Double(10, 2.2, 4, 4));
                g.draw(new Line2D.Double(12, 6.2, 12, 21));
                g.draw(new Line2D.Double(8.2, 9.6, 15.8, 9.6));
                g.draw(new Arc2D.Double(4, 5, 16, 16, 180, 180, Arc2D.OPEN));
                g.draw(new Line2D.Double(4, 13, 2.2, 10.2));
                g.draw(new Line2D.Double(4, 13, 6.4, 11.4));
                g.draw(new Line2D.Double(20, 13, 21.8, 10.2));
                g.draw(new Line2D.Double(20, 13, 17.6, 11.4));
            }
            case JUGAR -> {
                Path2D p = new Path2D.Double();
                p.moveTo(7, 4.5);
                p.lineTo(19.5, 12);
                p.lineTo(7, 19.5);
                p.closePath();
                g.draw(p);
            }
            case ENGRANAJE -> {
                Path2D p = new Path2D.Double();
                int dientes = 8;
                double exterior = 10, interior = 7.6;
                for (int i = 0; i < dientes; i++) {
                    double base = i * 2 * Math.PI / dientes;
                    double[][] pasos = {{-0.40, interior}, {-0.17, exterior}, {0.17, exterior}, {0.40, interior}};
                    for (double[] paso : pasos) {
                        double x = 12 + Math.cos(base + paso[0]) * paso[1];
                        double y = 12 + Math.sin(base + paso[0]) * paso[1];
                        if (i == 0 && paso == pasos[0]) {
                            p.moveTo(x, y);
                        } else {
                            p.lineTo(x, y);
                        }
                    }
                }
                p.closePath();
                g.draw(p);
                g.draw(new Ellipse2D.Double(8.8, 8.8, 6.4, 6.4));
            }
            case LIBRO -> {
                Path2D p = new Path2D.Double();
                p.moveTo(12, 6.5);
                p.curveTo(9.5, 4.8, 6, 4.5, 3, 5);
                p.lineTo(3, 18.5);
                p.curveTo(6, 18, 9.5, 18.3, 12, 20);
                p.curveTo(14.5, 18.3, 18, 18, 21, 18.5);
                p.lineTo(21, 5);
                p.curveTo(18, 4.5, 14.5, 4.8, 12, 6.5);
                g.draw(p);
                g.draw(new Line2D.Double(12, 6.5, 12, 20));
            }
            case SALIDA -> {
                Path2D p = new Path2D.Double();
                p.moveTo(10, 4);
                p.lineTo(5, 4);
                p.lineTo(5, 20);
                p.lineTo(10, 20);
                g.draw(p);
                g.draw(new Line2D.Double(9.5, 12, 20, 12));
                Path2D flecha = new Path2D.Double();
                flecha.moveTo(16, 8);
                flecha.lineTo(20, 12);
                flecha.lineTo(16, 16);
                g.draw(flecha);
            }
            case MARCADOR -> {
                Path2D p = new Path2D.Double();
                p.moveTo(6, 3);
                p.lineTo(18, 3);
                p.lineTo(18, 21);
                p.lineTo(12, 16.5);
                p.lineTo(6, 21);
                p.closePath();
                g.draw(p);
            }
            case PISTOLA -> {
                g.draw(new RoundRectangle2D.Double(2.5, 6, 18, 5, 2, 2));
                Path2D p = new Path2D.Double();
                p.moveTo(6, 11);
                p.lineTo(8, 19);
                p.lineTo(12.5, 19);
                p.lineTo(11.2, 11);
                g.draw(p);
                g.draw(new Line2D.Double(14, 11, 14, 13.5));
                g.draw(new Line2D.Double(14, 13.5, 16.5, 13.5));
            }
            case PERSONA -> persona(g);
            case PERSONAS -> {
                AffineTransform original = g.getTransform();
                g.translate(8, 1.2);
                g.scale(0.72, 0.72);
                persona(g);
                g.setTransform(original);
                g.translate(-1.2, 3.4);
                g.scale(0.80, 0.80);
                persona(g);
                g.setTransform(original);
            }
            case ATRAS -> {
                Path2D punta = new Path2D.Double();
                punta.moveTo(9.5, 5);
                punta.lineTo(4, 10.5);
                punta.lineTo(9.5, 16);
                g.draw(punta);
                Path2D cuerpo = new Path2D.Double();
                cuerpo.moveTo(4.5, 10.5);
                cuerpo.lineTo(14, 10.5);
                cuerpo.curveTo(19, 10.5, 20.5, 13.5, 20.5, 19);
                g.draw(cuerpo);
            }
            case BARRAS -> {
                double[][] barras = {{3.5, 11}, {8, 7}, {12.5, 3.5}, {17, 13}};
                for (double[] b : barras) {
                    g.draw(new RoundRectangle2D.Double(b[0], b[1], 3.5, 17.5 - b[1], 1, 1));
                }
                g.draw(new RoundRectangle2D.Double(2, 19.5, 20, 2.5, 1.2, 1.2));
            }
            case ALTAVOZ -> {
                Path2D p = new Path2D.Double();
                p.moveTo(3.5, 9.5);
                p.lineTo(8, 9.5);
                p.lineTo(13, 5);
                p.lineTo(13, 19);
                p.lineTo(8, 14.5);
                p.lineTo(3.5, 14.5);
                p.closePath();
                g.draw(p);
                g.draw(new Arc2D.Double(11.5, 8.5, 7, 7, -50, 100, Arc2D.OPEN));
                g.draw(new Arc2D.Double(10, 5.5, 13, 13, -50, 100, Arc2D.OPEN));
            }
        }
    }

    private static void persona(Graphics2D g) {
        g.draw(new Ellipse2D.Double(8, 3.5, 8, 8));
        g.draw(new Arc2D.Double(4, 14.5, 16, 14, 0, 180, Arc2D.OPEN));
    }
}
