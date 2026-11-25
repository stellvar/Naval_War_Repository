package co.edu.uptc.view.pantallas;

import co.edu.uptc.view.ControladorVista;
import co.edu.uptc.view.EstadisticasJugadorVista;
import co.edu.uptc.view.FilaRankingVista;
import co.edu.uptc.view.IdPantalla;
import co.edu.uptc.view.Navegador;
import co.edu.uptc.view.Tema;
import co.edu.uptc.view.Textos;
import co.edu.uptc.view.componentes.BarraFina;
import co.edu.uptc.view.componentes.BarraInferior;
import co.edu.uptc.view.componentes.BotonMarino;
import co.edu.uptc.view.componentes.CabeceraPantalla;
import co.edu.uptc.view.componentes.FilaRanking;
import co.edu.uptc.view.componentes.Iconos;
import co.edu.uptc.view.componentes.PanelEstadisticasJugador;
import co.edu.uptc.view.componentes.PanelTranslucido;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

/*ranking: mejores victorias (menos turnos) de los perfiles guardados */
public class PantallaEstadisticas extends Pantalla {

    public PantallaEstadisticas(Navegador nav, ControladorVista ctrl) {
        super(nav, ctrl);
    }

    @Override
    protected void construir() {
        add(new CabeceraPantalla(Textos.t("est.titulo")), BorderLayout.NORTH);

        PanelTranslucido panel = new PanelTranslucido(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(26, 44, 26, 44));
        panel.setPreferredSize(new Dimension(760, 330));
        panel.add(FilaRanking.encabezado(Textos.t("est.jugador"), Textos.t("est.turnos")), BorderLayout.NORTH);

        java.util.List<FilaRankingVista> ranking = ctrl.getRanking();
        if (ranking.isEmpty()) {
            JLabel vacio = new JLabel(Textos.t("est.vacio"), SwingConstants.CENTER);
            vacio.setFont(Tema.fuente(Font.BOLD, 17));
            vacio.setForeground(Tema.TEXTO);
            panel.add(vacio, BorderLayout.CENTER);
        } else {
            JPanel filas = new JPanel();
            filas.setOpaque(false);
            filas.setLayout(new BoxLayout(filas, BoxLayout.Y_AXIS));
            for (FilaRankingVista fila : ranking) {
                FilaRanking componente = FilaRanking.de(fila.nombre(), fila.avatar(), fila.turnos())
                        .alPulsar(() -> mostrarEstadisticas(fila.nombre()));
                componente.setMaximumSize(new Dimension(Integer.MAX_VALUE, componente.getPreferredSize().height));
                filas.add(componente);
            }
            JScrollPane scroll = new JScrollPane(filas, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                    ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            scroll.setOpaque(false);
            scroll.getViewport().setOpaque(false);
            scroll.setBorder(null);
            scroll.getVerticalScrollBar().setUI(new BarraFina());
            scroll.getVerticalScrollBar().setOpaque(false);
            panel.add(scroll, BorderLayout.CENTER);
        }
        add(centrado(panel, 12), BorderLayout.CENTER);

        BotonMarino menu = new BotonMarino(Textos.t("comun.menu"), Iconos.de(Iconos.Tipo.ANCLA, 28),
                BotonMarino.Forma.CHAFLAN, 300, 58, 22);
        menu.addActionListener(e -> nav.ir(IdPantalla.MENU));
        add(new BarraInferior(null, menu, null), BorderLayout.SOUTH);
    }

    /* ventana emergente con las estadisticas completas del jugador pulsado */
    private void mostrarEstadisticas(String nombre) {
        EstadisticasJugadorVista datos = ctrl.getEstadisticas(nombre);
        if (datos == null) {
            return;
        }
        nav.mostrarModal(new PanelEstadisticasJugador(datos, nav::cerrarModal), true, null, Tema.VELO_OSCURO, null);
    }
}

