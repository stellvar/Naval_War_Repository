package co.edu.uptc.view.componentes;

import java.awt.Color;

/* colores de cada bando*/
public enum EstiloFlota {
    PROPIO(new Color(0x1C2B7A), new Color(0x0B1450)),
    ENEMIGO(new Color(0x5E1C2E), new Color(0x35101C));

    public final Color fondo;
    public final Color borde;

    EstiloFlota(Color fondo, Color borde) {
        this.fondo = fondo;
        this.borde = borde;
    }
}
