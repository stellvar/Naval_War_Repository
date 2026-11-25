package co.edu.uptc.view.componentes;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.Icon;

import co.edu.uptc.view.Tema;

public final class Avatares {

    public static final int CANTIDAD = 6;
    public static final int POR_DEFECTO = 5;

    private static final String RUTA = "/img/avatares/avatar_";

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
        BufferedImage imagen = imagen(indice);
        if (imagen == null) {
            return;
        }
        Graphics2D g = (Graphics2D) destino.create();
        Tema.calidad(g);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.translate(x, y);
        g.clip(new RoundRectangle2D.Double(0, 0, tam, tam, tam * 0.12, tam * 0.12));
        g.drawImage(imagen, 0, 0, tam, tam, null);
        g.dispose();
    }

    private static synchronized BufferedImage imagen(int indice) {
        if (indice < 0 || indice >= IMAGENES.length) {
            return null;
        }
        if (!INTENTADO[indice]) {
            INTENTADO[indice] = true;
            String ruta = RUTA + indice + ".png";
            try (InputStream in = Avatares.class.getResourceAsStream(ruta)) {
                if (in == null) {
                    System.err.println("No se encontró el avatar " + ruta);
                } else {
                    IMAGENES[indice] = ImageIO.read(in);
                }
            } catch (Exception e) {
                System.err.println("No se pudo leer el avatar " + ruta + ": " + e.getMessage());
            }
        }
        return IMAGENES[indice];
    }
}
