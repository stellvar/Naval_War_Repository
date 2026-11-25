package co.edu.uptc.view.componentes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;

import javax.swing.Icon;

import co.edu.uptc.view.Tema;

/* iconos en java 2d */
public final class Iconos {

    public enum Tipo { ANCLA, JUGAR, ENGRANAJE, LIBRO, SALIDA, MARCADOR, PISTOLA, PERSONA, PERSONAS, ATRAS, ALTAVOZ, BARRAS }

    /* ancla rellena (sin borde) en un sistema de coordenadas de 100x100, simetrica respecto a x = 50 */
    private static final Shape ANCLA = crearAncla();

    private Iconos() {
    }

    private static Shape crearAncla() {
        // anillo superior: circulo exterior menos el agujero interior
        Area ancla = new Area(new Ellipse2D.Double(40, 8, 20, 20));
        ancla.subtract(new Area(new Ellipse2D.Double(45.5, 13.5, 9, 9)));

        Path2D.Double cuerpo = new Path2D.Double();
        // lado derecho del mastil y cepo derecho
        cuerpo.moveTo(54.5, 26);
        cuerpo.lineTo(54.5, 32);
        cuerpo.lineTo(71, 32);
        cuerpo.lineTo(71, 40);
        cuerpo.lineTo(54.5, 40);
        cuerpo.lineTo(54.5, 76);
        // brazo derecho y punta de flecha
        cuerpo.quadTo(67, 75, 75, 62);
        cuerpo.lineTo(69, 59);
        cuerpo.lineTo(85, 50);
        cuerpo.lineTo(86, 68);
        cuerpo.lineTo(81, 65);
        // curva inferior (cruz)
        cuerpo.quadTo(71, 83, 50, 91);
        cuerpo.quadTo(29, 83, 19, 65);
        // brazo izquierdo y punta de flecha
        cuerpo.lineTo(14, 68);
        cuerpo.lineTo(15, 50);
        cuerpo.lineTo(31, 59);
        cuerpo.lineTo(25, 62);
        cuerpo.quadTo(33, 75, 45.5, 76);
        // lado izquierdo del mastil y cepo izquierdo
        cuerpo.lineTo(45.5, 40);
        cuerpo.lineTo(29, 40);
        cuerpo.lineTo(29, 32);
        cuerpo.lineTo(45.5, 32);
        cuerpo.lineTo(45.5, 26);
        cuerpo.closePath();

        ancla.add(new Area(cuerpo));
        return ancla;
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
                /* forma rellena de 100x100 reducida a la cuadricula de 24 */
                g.scale(0.24, 0.24);
                g.fill(ANCLA);
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
