package co.edu.uptc.view;

import co.edu.uptc.view.componentes.Iconos;

import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;


public class VentanaPrincipal extends JFrame {

    private final Escritorio escritorio;

    public VentanaPrincipal() {
        super("Batalla Naval");
        escritorio = new Escritorio();
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
        setMinimumSize(new Dimension(1100, 660));
        pack();
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
