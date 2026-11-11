package co.edu.uptc.view.pantallas;

import co.edu.uptc.view.IdPantalla;
import co.edu.uptc.view.Navegador;
import co.edu.uptc.view.Textos;
import co.edu.uptc.view.componentes.BarraInferior;
import co.edu.uptc.view.componentes.BotonMarino;
import co.edu.uptc.view.componentes.CabeceraPantalla;
import co.edu.uptc.view.componentes.FilaRanking;
import co.edu.uptc.view.componentes.Iconos;
import co.edu.uptc.view.componentes.PanelTranslucido;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import java.awt.BorderLayout;
import java.awt.Dimension;

/*ranking */
public class PantallaEstadisticas extends Pantalla {


    private static final Object[][] EJEMPLO = {
            {"Player1", 0, 24},
            {"Capitán Nemo", 2, 31},
            {"Barbanegra", 3, 38},
            {"Marinero", 5, 45}
    };

    public PantallaEstadisticas(Navegador nav) {
        super(nav);
    }

    @Override
    protected void construir() {
        add(new CabeceraPantalla(Textos.t("est.titulo")), BorderLayout.NORTH);

        PanelTranslucido panel = new PanelTranslucido(null);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(26, 44, 26, 44));
        panel.setPreferredSize(new Dimension(760, 330));
        panel.add(FilaRanking.encabezado(Textos.t("est.jugador"), Textos.t("est.turnos")));

        for (Object[] fila : EJEMPLO) {
            panel.add(FilaRanking.de((String) fila[0], (Integer) fila[1], (Integer) fila[2]));
        }
        add(centrado(panel, 12), BorderLayout.CENTER);

        BotonMarino menu = new BotonMarino(Textos.t("comun.menu"), Iconos.de(Iconos.Tipo.ANCLA, 28),
                BotonMarino.Forma.PILDORA, 300, 58, 22);
        menu.addActionListener(e -> nav.ir(IdPantalla.MENU));
        add(new BarraInferior(null, menu, null), BorderLayout.SOUTH);
    }
}
