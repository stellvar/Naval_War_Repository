package co.edu.uptc.view.pantallas;

import co.edu.uptc.view.IdPantalla;
import co.edu.uptc.view.Navegador;
import co.edu.uptc.view.Tema;
import co.edu.uptc.view.Textos;
import co.edu.uptc.view.componentes.AreaRedondeada;
import co.edu.uptc.view.componentes.BarraInferior;
import co.edu.uptc.view.componentes.BotonMarino;
import co.edu.uptc.view.componentes.CabeceraPantalla;
import co.edu.uptc.view.componentes.CampoRedondeado;
import co.edu.uptc.view.componentes.Iconos;
import co.edu.uptc.view.componentes.PanelTranslucido;
import co.edu.uptc.view.componentes.SelectorAvatar;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

/*(Jugador 1 y Jugador 2): nombre de perfil, biografia y avatar */
public class PantallaPersonalizar extends Pantalla {

    private int numero = 1;
    private Runnable alGuardar;

    private CampoRedondeado campoNombre;
    private AreaRedondeada areaBio;
    private SelectorAvatar selector;
    private JLabel error;

    public PantallaPersonalizar(Navegador nav) {
        super(nav);
    }

    public void configurar(int numero, Runnable alGuardar) {
        this.numero = numero;
        this.alGuardar = alGuardar;
    }

    @Override
    protected void construir() {
        add(new CabeceraPantalla(Textos.t("perfil.titulo")), BorderLayout.NORTH);

        campoNombre = new CampoRedondeado("Player" + numero, 24);
        campoNombre.setPreferredSize(new Dimension(330, 40));
        campoNombre.setMaximumSize(new Dimension(330, 40));
        campoNombre.setAlignmentX(CENTER_ALIGNMENT);
        areaBio = new AreaRedondeada(140);
        areaBio.setPreferredSize(new Dimension(330, 112));
        areaBio.setMaximumSize(new Dimension(330, 112));
        areaBio.setAlignmentX(CENTER_ALIGNMENT);
        selector = new SelectorAvatar(92);
        error = new JLabel(" ", SwingConstants.CENTER);
        error.setFont(Tema.fuente(Font.BOLD, 15));
        error.setForeground(Tema.ERROR);

        JPanel izquierda = new JPanel();
        izquierda.setOpaque(false);
        izquierda.setLayout(new BoxLayout(izquierda, BoxLayout.Y_AXIS));
        izquierda.add(etiqueta(Textos.t("perfil.jugador", numero), Tema.CIAN, 21));
        izquierda.add(Box.createVerticalStrut(14));
        izquierda.add(etiqueta(Textos.t("perfil.nombre"), Tema.TEXTO, 18));
        izquierda.add(Box.createVerticalStrut(6));
        izquierda.add(campoNombre);
        izquierda.add(Box.createVerticalStrut(16));
        izquierda.add(etiqueta(Textos.t("perfil.bio"), Tema.TEXTO, 18));
        izquierda.add(Box.createVerticalStrut(6));
        izquierda.add(areaBio);

        JPanel derecha = new JPanel();
        derecha.setOpaque(false);
        derecha.setLayout(new BoxLayout(derecha, BoxLayout.Y_AXIS));
        derecha.add(etiqueta(Textos.t("perfil.avatar"), Tema.CIAN, 21));
        derecha.add(Box.createVerticalStrut(22));
        selector.setAlignmentX(CENTER_ALIGNMENT);
        selector.setMaximumSize(selector.getPreferredSize());
        derecha.add(selector);

        PanelTranslucido panel = new PanelTranslucido(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(24, 40, 12, 40));
        panel.setPreferredSize(new Dimension(940, 400));
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1;
        c.weighty = 1;
        c.gridx = 0;
        c.gridy = 0;
        panel.add(izquierda, c);
        c.gridx = 1;
        panel.add(derecha, c);
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.weighty = 0;
        panel.add(error, c);
        add(centrado(panel, 4), BorderLayout.CENTER);

        BotonMarino menu = new BotonMarino(Textos.t("comun.menu"), Iconos.de(Iconos.Tipo.ANCLA, 28),
                BotonMarino.Forma.PILDORA, 270, 58, 22);
        menu.addActionListener(e -> nav.ir(IdPantalla.MENU));
        BotonMarino guardar = new BotonMarino(Textos.t("comun.guardar"), Iconos.de(Iconos.Tipo.MARCADOR, 28),
                BotonMarino.Forma.PILDORA, 270, 58, 22);
        guardar.addActionListener(e -> guardar());
        add(new BarraInferior(menu, null, guardar), BorderLayout.SOUTH);
    }

    private JLabel etiqueta(String texto, java.awt.Color color, float tamano) {
        JLabel etiqueta = new JLabel(texto, SwingConstants.CENTER);
        etiqueta.setFont(Tema.fuente(Font.BOLD, tamano));
        etiqueta.setForeground(color);
        etiqueta.setAlignmentX(CENTER_ALIGNMENT);
        return etiqueta;
    }


    private void guardar() {
        if (alGuardar != null) {
            alGuardar.run();
        }
    }
}
