package co.edu.uptc.view.pantallas;

import co.edu.uptc.view.IdPantalla;
import co.edu.uptc.view.Navegador;
import co.edu.uptc.view.Tema;
import co.edu.uptc.view.Textos;
import co.edu.uptc.view.componentes.BarraInferior;
import co.edu.uptc.view.componentes.BotonMarino;
import co.edu.uptc.view.componentes.CabeceraPantalla;
import co.edu.uptc.view.componentes.Iconos;
import co.edu.uptc.view.componentes.OpcionIcono;
import co.edu.uptc.view.componentes.PanelSonido;
import co.edu.uptc.view.componentes.PanelTranslucido;
import co.edu.uptc.view.componentes.SelectorIdioma;

import javax.swing.Box;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/* configuración de sonido, perfil e idioma*/
public class PantallaOpciones extends Pantalla {

    public PantallaOpciones(Navegador nav) {
        super(nav);
    }

    @Override
    protected void construir() {
        add(new CabeceraPantalla(Textos.t("opciones.titulo")), BorderLayout.NORTH);

        PanelTranslucido panel = new PanelTranslucido(new GridBagLayout());
        panel.setPreferredSize(new Dimension(940, 330));
        panel.setBorder(BorderFactory.createEmptyBorder(26, 40, 26, 40));
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.insets = new Insets(0, 0, 0, 30);
        panel.add(new PanelSonido(false), c);
        c.gridx = 1;
        c.insets = new Insets(0, 30, 0, 0);
        panel.add(columnaSocial(), c);
        add(centrado(panel, 12), BorderLayout.CENTER);

        BotonMarino menu = new BotonMarino(Textos.t("comun.menu"), Iconos.de(Iconos.Tipo.ANCLA, 28),
                BotonMarino.Forma.PILDORA, 300, 58, 22);
        menu.addActionListener(e -> nav.ir(IdPantalla.MENU));
        add(new BarraInferior(null, menu, null), BorderLayout.SOUTH);
    }

    private JPanel columnaSocial() {
        JPanel columna = new JPanel();
        columna.setOpaque(false);
        columna.setLayout(new BoxLayout(columna, BoxLayout.Y_AXIS));

        JLabel titulo = etiqueta(Textos.t("opciones.social"), 18);
        columna.add(titulo);
        columna.add(Box.createVerticalStrut(10));

        OpcionIcono personalizar = new OpcionIcono(Textos.t("opciones.personalizar"),
                Iconos.de(Iconos.Tipo.PERSONAS, 62), 230, 118);
        personalizar.setAlignmentX(Component.LEFT_ALIGNMENT);
        personalizar.addActionListener(e -> nav.editarPerfil(1, () -> nav.ir(IdPantalla.OPCIONES)));
        columna.add(personalizar);
        columna.add(Box.createVerticalStrut(10));

        JLabel idioma = etiqueta(Textos.t("opciones.idioma"), 17);
        columna.add(idioma);
        columna.add(Box.createVerticalStrut(6));

        SelectorIdioma selector = new SelectorIdioma(Textos.getIdioma(), codigo -> {
            Textos.setIdioma(codigo);
            nav.recargar();
        });
        selector.setAlignmentX(Component.LEFT_ALIGNMENT);
        selector.setMaximumSize(new Dimension(230, 34));
        selector.setPreferredSize(new Dimension(230, 34));
        columna.add(selector);
        return columna;
    }

    private JLabel etiqueta(String texto, float tamano) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(Tema.fuente(Font.BOLD, tamano));
        etiqueta.setForeground(Tema.TEXTO);
        etiqueta.setAlignmentX(Component.LEFT_ALIGNMENT);
        return etiqueta;
    }
}
