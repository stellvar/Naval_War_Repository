package co.edu.uptc.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/*
 Paleta, tipografía y utilidades de dibujo
 */
public final class Tema {

    public static final Color CIAN = new Color(0xA2E7FF);
    public static final Color TEXTO = new Color(0xEAF7FF);
    public static final Color PANEL = new Color(5, 32, 62, 215);
    public static final Color BOTON = new Color(0x041E3A);
    public static final Color BOTON_HOVER = new Color(0x0A3560);
    public static final Color BOTON_PRESIONADO = new Color(0x021426);
    public static final Color BOTON_BORDE = new Color(0x1B4F85);
    /* velo estándar de todas las ventanas emergentes: oscuro y translúcido, deja ver lo que hay debajo */
    public static final Color VELO_OSCURO = new Color(3, 14, 30, 180);
    /* velo opaco: tapa por completo la pantalla (cambio de turno, para no ver la flota del rival)
       y deja ver en su lugar el fondo predeterminado oscurecido */
    public static final Color VELO_TAPADO = new Color(3, 14, 30, 255);
    /* panel de las ventanas emergentes: un tono más claro que el velo */
    public static final Color PANEL_MODAL = new Color(9, 44, 84);
    public static final Color CAMPO = new Color(0x8A96A6);
    public static final Color CAMPO_TEXTO = new Color(0x08203A);
    public static final Color IMPACTO = new Color(0xE11D2E);
    public static final Color ERROR = new Color(0xFFA3AD);

    /* botones principales (la acción que queremos que el usuario elija: jugar, continuar, listo...):
       estilo invertido del botón regular, fondo claro y contenido oscuro */
    public static final Color ACCION = CIAN;
    public static final Color ACCION_HOVER = TEXTO;
    public static final Color ACCION_PRESIONADO = CIAN.darker();
    public static final Color ACCION_BORDE = TEXTO;
    public static final Color ACCION_TEXTO = BOTON;

    /* fondo circular de los botones de icono (engranaje en partida) */
    public static final Color CIRCULO = new Color(2, 14, 32, 150);
    public static final Color CIRCULO_HOVER = new Color(10, 45, 85, 200);
    public static final Color CIRCULO_ACTIVO = new Color(4, 30, 58, 245);

    private static final String FAMILIA = elegirFamilia();

    private Tema() {
    }

    private static String elegirFamilia() {
        Set<String> disponibles = new HashSet<>(Arrays.asList(
                GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()));
        for (String familia : new String[]{"Montserrat", "Poppins", "Segoe UI", "Helvetica Neue", "Arial"}) {
            if (disponibles.contains(familia)) {
                return familia;
            }
        }
        return Font.SANS_SERIF;
    }

    public static String familia() {
        return FAMILIA;
    }

    public static Font fuente(int estilo, float tamano) {
        return new Font(FAMILIA, estilo, Math.round(tamano));
    }

    public static void calidad(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    }
}
