package co.edu.uptc.view.componentes;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.InputStream;

/* panel que pinta la imagen de fondo (/img/fondo.jpg) */
public class PanelFondo extends JPanel {

    private static BufferedImage original;
    private static BufferedImage cache;

    static {
        try (InputStream in = PanelFondo.class.getResourceAsStream("/img/fondo.jpg")) {
            if (in != null) {
                original = ImageIO.read(in);
            }
        } catch (Exception e) {
            original = null;
        }
    }

    public PanelFondo() {
        setOpaque(true);
        setBackground(new Color(0x07254A));
    }

    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0.create();
        int w = getWidth();
        int h = getHeight();
        if (original == null) {
            g.setPaint(new GradientPaint(0, 0, new Color(0x0B3A78), 0, h, new Color(0x041A38)));
            g.fillRect(0, 0, w, h);
        } else {
            g.drawImage(escalada(w, h), 0, 0, null);
        }
        g.dispose();
    }

    /* escala centrada */
    private static synchronized BufferedImage escalada(int w, int h) {
        if (cache != null && cache.getWidth() == w && cache.getHeight() == h) {
            return cache;
        }
        double escala = Math.max((double) w / original.getWidth(), (double) h / original.getHeight());
        int sw = (int) Math.ceil(original.getWidth() * escala);
        int sh = (int) Math.ceil(original.getHeight() * escala);
        BufferedImage salida = new BufferedImage(Math.max(1, w), Math.max(1, h), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = salida.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.drawImage(original, (w - sw) / 2, (h - sh) / 2, sw, sh, null);
        g.dispose();
        cache = salida;
        return cache;
    }
}
