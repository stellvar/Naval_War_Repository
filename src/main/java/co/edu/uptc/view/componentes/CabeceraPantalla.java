package co.edu.uptc.view.componentes;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import co.edu.uptc.view.Tema;


public class CabeceraPantalla extends JPanel {

    private static final int DIAMETRO = 92;
    /* el png trae su propio borde: ocupa todo el diametro */
    private static final int LADO_LOGO = DIAMETRO;
    /* png del emblema superior: src/main/resources/img/logo.png */
    private static final BufferedImage LOGO = cargarLogo();
    private final JLabel titulo;

    public CabeceraPantalla(String texto) {
        this(texto, 36, 166);
    }

    /* cabecera con título de otro tamaño; un alto mayor lo baja, acercándolo al contenido (menú principal) */
    public CabeceraPantalla(String texto, float tamanoTitulo, int alto) {
        setOpaque(false);
        setLayout(new BorderLayout());
        titulo = new JLabel(texto, SwingConstants.CENTER);
        titulo.setFont(Tema.fuente(java.awt.Font.BOLD, tamanoTitulo));
        titulo.setForeground(Tema.CIAN);
        titulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        add(titulo, BorderLayout.SOUTH);
        setPreferredSize(new Dimension(100, alto));
    }

    private static BufferedImage cargarLogo() {
        try (InputStream in = CabeceraPantalla.class.getResourceAsStream("/img/logo.png")) {
            if (in == null) {
                System.err.println("No se encontró /img/logo.png");
                return null;
            }
            return ImageIO.read(in);
        } catch (Exception e) {
            System.err.println("No se pudo leer /img/logo.png: " + e.getMessage());
            return null;
        }
    }

    public void setTitulo(String texto) {
        titulo.setText(texto);
    }

    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0.create();
        Tema.calidad(g);
        int w = getWidth();
        int cx = w / 2;
        int cy = 12 + DIAMETRO / 2;

        double largo = Math.min(w * 0.22, 320);
        int separacion = 26;
        g.setColor(new Color(255, 255, 255, 215));
        g.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        double xi = cx - DIAMETRO / 2.0 - separacion;
        g.draw(new Line2D.Double(xi - largo, cy, xi, cy));
        g.draw(new Line2D.Double(xi - largo, cy, xi - largo + 8, cy - 4));
        g.draw(new Line2D.Double(xi - largo, cy, xi - largo + 8, cy + 4));
        double xd = cx + DIAMETRO / 2.0 + separacion;
        g.draw(new Line2D.Double(xd, cy, xd + largo, cy));
        g.draw(new Line2D.Double(xd + largo, cy, xd + largo - 8, cy - 4));
        g.draw(new Line2D.Double(xd + largo, cy, xd + largo - 8, cy + 4));

        if (LOGO != null) {
            double escala = Math.min((double) LADO_LOGO / LOGO.getWidth(), (double) LADO_LOGO / LOGO.getHeight());
            int ancho = (int) Math.round(LOGO.getWidth() * escala);
            int alto = (int) Math.round(LOGO.getHeight() * escala);
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g.drawImage(LOGO, cx - ancho / 2, cy - alto / 2, ancho, alto, null);
        }
        g.dispose();
    }
}
