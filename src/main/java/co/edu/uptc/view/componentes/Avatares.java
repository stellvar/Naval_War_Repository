package co.edu.uptc.view.componentes;

import co.edu.uptc.view.Tema;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.io.InputStream;


public final class Avatares {

    public static final int CANTIDAD = 6;
    public static final int POR_DEFECTO = 5;

    private static final BufferedImage[] IMAGENES = new BufferedImage[CANTIDAD];
    private static final boolean[] INTENTADO = new boolean[CANTIDAD];

    private Avatares() {
    }

    public static Icon icono(int indice, int tam) {
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
                dibujar((Graphics2D) g, indice, x, y, tam);
            }
        };
    }

    public static void dibujar(Graphics2D destino, int indice, int x, int y, int tam) {
        Graphics2D g = (Graphics2D) destino.create();
        Tema.calidad(g);
        g.translate(x, y);
        g.clip(new RoundRectangle2D.Double(0, 0, tam, tam, tam * 0.12, tam * 0.12));
        BufferedImage imagen = imagen(indice);
        if (imagen != null) {
            g.drawImage(imagen, 0, 0, tam, tam, null);
        } else {
            g.scale(tam / 64.0, tam / 64.0);
            generado(g, indice);
        }
        g.dispose();
    }

    private static synchronized BufferedImage imagen(int indice) {
        if (indice < 0 || indice >= IMAGENES.length) {
            return null;
        }
        if (!INTENTADO[indice]) {
            INTENTADO[indice] = true;
            try (InputStream in = Avatares.class.getResourceAsStream("/img/avatares/avatar_" + indice + ".png")) {
                if (in != null) {
                    IMAGENES[indice] = ImageIO.read(in);
                }
            } catch (Exception e) {
                IMAGENES[indice] = null;
            }
        }
        return IMAGENES[indice];
    }

    // ---- ilustraciones vectoriales----

    private static void generado(Graphics2D g, int indice) {
        switch (indice) {
            case 0 -> {
                fondo(g, new Color(0x4B7DB3), new Color(0x22395E));
                busto(g, null, new Color(0xE3B48F), new Color(0x12233F));
                rostro(g);
                gorra(g, Color.WHITE);
            }
            case 1 -> {
                fondo(g, new Color(0x5D8AB8), new Color(0x27406A));
                busto(g, new Color(0x6B3F25), new Color(0xF0C8A8), new Color(0x12233F));
                rostro(g);
                gorra(g, Color.WHITE);
            }
            case 2 -> {
                fondo(g, new Color(0xF29A4A), new Color(0x7A3A32));
                busto(g, null, new Color(0xC48A5E), new Color(0x2A2F3A));
                barba(g, new Color(0x3A261A));
                gafas(g);
                gorra(g, new Color(0x1B2230));
            }
            case 3 -> calavera(g);
            case 4 -> {
                fondo(g, new Color(0x6D88A8), new Color(0x2B3F5E));
                g.setColor(new Color(0x1E2A3A));
                g.fill(new Ellipse2D.Double(14, 10, 36, 42));
                busto(g, null, new Color(0xB98A68), new Color(0x1E2A3A));
                barba(g, new Color(0x2B211A));
                rostro(g);
            }
            default -> emblema(g);
        }
    }

    private static void fondo(Graphics2D g, Color arriba, Color abajo) {
        g.setPaint(new GradientPaint(0, 0, arriba, 0, 64, abajo));
        g.fill(new Rectangle2D.Double(0, 0, 64, 64));
    }

    private static void busto(Graphics2D g, Color pelo, Color piel, Color uniforme) {
        if (pelo != null) {
            g.setColor(pelo);
            g.fill(new Ellipse2D.Double(16, 12, 32, 40));
        }
        g.setColor(uniforme);
        g.fill(new Ellipse2D.Double(6, 46, 52, 40));
        g.setColor(piel.darker());
        g.fill(new Rectangle2D.Double(27, 38, 10, 11));
        g.setColor(piel);
        g.fill(new Ellipse2D.Double(20, 15, 24, 29));
    }

    private static void rostro(Graphics2D g) {
        g.setColor(new Color(0x1A1410));
        g.fill(new Ellipse2D.Double(25.5, 29, 3.2, 3.2));
        g.fill(new Ellipse2D.Double(35.3, 29, 3.2, 3.2));
        g.setStroke(new BasicStroke(1.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.draw(new Line2D.Double(28.5, 38, 35.5, 38));
    }

    private static void gorra(Graphics2D g, Color color) {
        g.setColor(color);
        g.fill(new RoundRectangle2D.Double(18, 7, 28, 15, 10, 10));
        g.fill(new RoundRectangle2D.Double(16, 19, 32, 5, 4, 4));
        g.setColor(new Color(0xE6B93C));
        g.fill(new Ellipse2D.Double(29.5, 12, 5, 5));
    }

    private static void gafas(Graphics2D g) {
        g.setColor(new Color(0x111111));
        g.fill(new RoundRectangle2D.Double(21, 26, 9, 6, 3, 3));
        g.fill(new RoundRectangle2D.Double(34, 26, 9, 6, 3, 3));
        g.fill(new Rectangle2D.Double(29.5, 27.5, 5, 1.6));
    }

    private static void barba(Graphics2D g, Color color) {
        g.setColor(color);
        g.fill(new Arc2D.Double(20.5, 22, 23, 26, 200, 140, Arc2D.CHORD));
    }

    private static void calavera(Graphics2D g) {
        fondo(g, new Color(0x15151B), new Color(0x2B241A));
        g.setColor(new Color(0xE6B93C));
        g.setStroke(new BasicStroke(5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.draw(new Line2D.Double(13, 43, 51, 58));
        g.draw(new Line2D.Double(51, 43, 13, 58));
        g.setColor(new Color(0xF1ECE0));
        g.fill(new Ellipse2D.Double(17, 8, 30, 30));
        g.fill(new RoundRectangle2D.Double(24, 31, 16, 13, 4, 4));
        g.setColor(new Color(0x15151B));
        g.fill(new Ellipse2D.Double(22, 20, 8, 9));
        g.fill(new Ellipse2D.Double(34, 20, 8, 9));
        Path2D nariz = new Path2D.Double();
        nariz.moveTo(32, 29);
        nariz.lineTo(29.5, 34);
        nariz.lineTo(34.5, 34);
        nariz.closePath();
        g.fill(nariz);
        g.setStroke(new BasicStroke(1.2f));
        for (double x = 28; x <= 36; x += 4) {
            g.draw(new Line2D.Double(x, 38, x, 43));
        }
    }

    private static void emblema(Graphics2D g) {
        fondo(g, new Color(0x123A6B), new Color(0x0B2244));
        g.setColor(Color.WHITE);
        g.setStroke(new BasicStroke(3f));
        g.draw(new Ellipse2D.Double(7, 7, 50, 50));
        Iconos.de(Iconos.Tipo.ANCLA, 30, Color.WHITE).paintIcon(null, g, 17, 11);
        g.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.draw(new Arc2D.Double(14, 42, 14, 6, 0, 180, Arc2D.OPEN));
        g.draw(new Arc2D.Double(26, 42, 14, 6, 180, 180, Arc2D.OPEN));
        g.draw(new Arc2D.Double(38, 42, 12, 6, 0, 180, Arc2D.OPEN));
    }
}
