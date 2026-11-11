package co.edu.uptc.view.pantallas;

import co.edu.uptc.view.IdPantalla;
import co.edu.uptc.view.Navegador;
import co.edu.uptc.view.Tema;
import co.edu.uptc.view.Textos;
import co.edu.uptc.view.componentes.BarraFina;
import co.edu.uptc.view.componentes.BarraInferior;
import co.edu.uptc.view.componentes.BotonMarino;
import co.edu.uptc.view.componentes.CabeceraPantalla;
import co.edu.uptc.view.componentes.EstiloFlota;
import co.edu.uptc.view.componentes.Iconos;
import co.edu.uptc.view.componentes.PanelTranslucido;
import co.edu.uptc.view.componentes.TableroPanel;
import co.edu.uptc.view.componentes.TableroVista;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;

/*reglas y miniaturas */
public class PantallaGuia extends Pantalla {

    public PantallaGuia(Navegador nav) {
        super(nav);
    }

    @Override
    protected void construir() {
        add(new CabeceraPantalla(Textos.t("guia.titulo")), BorderLayout.NORTH);

        JEditorPane texto = new JEditorPane("text/html", html());
        texto.setEditable(false);
        texto.setOpaque(false);
        texto.setBackground(new Color(0, 0, 0, 0));
        texto.setCaretPosition(0);
        JScrollPane scroll = new JScrollPane(texto, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUI(new BarraFina());
        scroll.getVerticalScrollBar().setOpaque(false);

        PanelTranslucido panel = new PanelTranslucido(new BorderLayout(26, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(16, 32, 16, 26));
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(vistaPrevia(), BorderLayout.EAST);
        add(expandido(panel, new Insets(0, 80, 6, 80)), BorderLayout.CENTER);

        BotonMarino menu = new BotonMarino(Textos.t("comun.menu"), Iconos.de(Iconos.Tipo.ANCLA, 28),
                BotonMarino.Forma.PILDORA, 250, 56, 20);
        menu.addActionListener(e -> nav.ir(IdPantalla.MENU));
        BotonMarino jugar = new BotonMarino(Textos.t("comun.jugar"), Iconos.de(Iconos.Tipo.PISTOLA, 28),
                BotonMarino.Forma.PILDORA, 250, 56, 20);
        jugar.addActionListener(e -> nav.ir(IdPantalla.SELECCION));
        add(new BarraInferior(menu, null, jugar), BorderLayout.SOUTH);
    }

    private String html() {
        StringBuilder sb = new StringBuilder("<html><body style='font-family:\"" + Tema.familia()
                + "\",sans-serif;font-size:13px;color:#EAF7FF;margin:0'>");
        seccion(sb, "guia.objetivo", 1);
        seccion(sb, "guia.prep", 2);
        seccion(sb, "guia.comb", 4);
        seccion(sb, "guia.vic", 1);
        return sb.append("</body></html>").toString();
    }

    private void seccion(StringBuilder sb, String base, int items) {
        sb.append("<p style='margin:7px 0 2px 0;color:#A2E7FF;font-size:15px'><b>")
                .append(Textos.t(base + ".t")).append("</b></p>");
        for (int i = 1; i <= items; i++) {
            String[] partes = Textos.t(base + "." + i).split("\\|", 2);
            sb.append("<p style='margin:0 0 3px 0'>");
            if (partes.length == 2) {
                sb.append("<b>").append(partes[0]).append(":</b> ").append(partes[1]);
            } else {
                sb.append(partes[0]);
            }
            sb.append("</p>");
        }
    }


    private JPanel vistaPrevia() {
        JPanel columna = new JPanel();
        columna.setOpaque(false);
        columna.setLayout(new BoxLayout(columna, BoxLayout.Y_AXIS));
        TableroPanel propio = new TableroPanel(EstiloFlota.PROPIO);
        propio.setPreferredSize(new Dimension(270, 178));
        propio.setMaximumSize(new Dimension(270, 178));
        propio.setTablero(TableroVista.demoPropio(), true);
        TableroPanel enemigo = new TableroPanel(EstiloFlota.ENEMIGO);
        enemigo.setPreferredSize(new Dimension(270, 178));
        enemigo.setMaximumSize(new Dimension(270, 178));
        enemigo.setTablero(TableroVista.demoEnemigo(), false);
        columna.add(propio);
        columna.add(javax.swing.Box.createVerticalStrut(14));
        columna.add(enemigo);
        return columna;
    }
}
