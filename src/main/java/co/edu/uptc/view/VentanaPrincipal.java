package co.edu.uptc.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import co.edu.uptc.view.componentes.Iconos;


public class VentanaPrincipal extends JFrame {

    private final Escritorio escritorio;

    public VentanaPrincipal(ControladorVista ctrl) {
        super("Batalla Naval");
        escritorio = new Escritorio(ctrl);
        setContentPane(escritorio);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
            }
        });
        setIconImage(icono());
        Rectangle util = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        setMinimumSize(new Dimension(Math.min(1100, util.width), Math.min(660, util.height)));
        pack();
        setSize(Math.min(getWidth(), util.width), Math.min(getHeight(), util.height));
        setLocationRelativeTo(null);
    }

    public Escritorio getEscritorio() {
        return escritorio;
    }

    private static BufferedImage icono() {
        BufferedImage imagen = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = imagen.createGraphics();
        Tema.calidad(g);
        g.setColor(new Color(0x0B3A78));
        g.fillOval(1, 1, 62, 62);
        Iconos.de(Iconos.Tipo.ANCLA, 44, Color.WHITE).paintIcon(null, g, 10, 10);
        g.dispose();
        return imagen;
    }
}
