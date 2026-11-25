package co.edu.uptc.view.componentes;

import co.edu.uptc.view.Tema;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Component;
import java.awt.Font;

/*ventana emergente con titulo y botones */
public class DialogoBotones extends TarjetaModal {

    public DialogoBotones(String titulo) {
        JLabel etiqueta = new JLabel(titulo, SwingConstants.CENTER);
        etiqueta.setFont(Tema.fuente(Font.BOLD, 27));
        etiqueta.setForeground(Tema.TEXTO);
        etiqueta.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(etiqueta);
        add(Box.createVerticalStrut(18));
    }

    public void setMensaje(String mensaje) {
        JLabel etiqueta = new JLabel("<html><div style='text-align:center;width:330px'>" + mensaje + "</div></html>",
                SwingConstants.CENTER);
        etiqueta.setFont(Tema.fuente(Font.PLAIN, 16));
        etiqueta.setForeground(Tema.CIAN);
        etiqueta.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(etiqueta);
        add(Box.createVerticalStrut(16));
    }

    public BotonMarino agregarBoton(String texto, Icon icono, Runnable accion) {
        return agregar(new BotonMarino(texto, icono, BotonMarino.Forma.CHAFLAN, 290, 49, 17), accion);
    }

    /* botón destacado: la acción que se espera que elija el usuario */
    public BotonMarino agregarBotonPrincipal(String texto, Icon icono, Runnable accion) {
        return agregar(new BotonMarino(texto, icono, BotonMarino.Forma.CHAFLAN, 290, 49, 17).principal(), accion);
    }

    private BotonMarino agregar(BotonMarino boton, Runnable accion) {
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.addActionListener(e -> accion.run());
        add(boton);
        add(Box.createVerticalStrut(12));
        return boton;
    }
}
