package co.edu.uptc.view;

import co.edu.uptc.view.componentes.SuperposicionModal;
import co.edu.uptc.view.pantallas.Pantalla;
import co.edu.uptc.view.pantallas.PantallaEstadisticas;
import co.edu.uptc.view.pantallas.PantallaGuia;
import co.edu.uptc.view.pantallas.PantallaJuego;
import co.edu.uptc.view.pantallas.PantallaMenu;
import co.edu.uptc.view.pantallas.PantallaOpciones;
import co.edu.uptc.view.pantallas.PantallaPersonalizar;
import co.edu.uptc.view.pantallas.PantallaSeleccionJugadores;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.EnumMap;
import java.util.Map;

/* contenedor de toda la interfaz */
public class Escritorio extends JLayeredPane implements Navegador {

    private final CardLayout cartas = new CardLayout();
    private final JPanel contenedor = new JPanel(cartas);
    private final SuperposicionModal modal = new SuperposicionModal();
    private final Map<IdPantalla, Pantalla> pantallas = new EnumMap<>(IdPantalla.class);
    private Pantalla actual;

    public Escritorio() {
        setOpaque(true);
        setBackground(new Color(0x07254A));
        pantallas.put(IdPantalla.MENU, new PantallaMenu(this));
        pantallas.put(IdPantalla.GUIA, new PantallaGuia(this));
        pantallas.put(IdPantalla.OPCIONES, new PantallaOpciones(this));
        pantallas.put(IdPantalla.ESTADISTICAS, new PantallaEstadisticas(this));
        pantallas.put(IdPantalla.SELECCION, new PantallaSeleccionJugadores(this));
        pantallas.put(IdPantalla.PERFIL, new PantallaPersonalizar(this));
        pantallas.put(IdPantalla.JUEGO, new PantallaJuego(this));
        for (Map.Entry<IdPantalla, Pantalla> e : pantallas.entrySet()) {
            contenedor.add(e.getValue(), e.getKey().name());
        }
        add(contenedor, DEFAULT_LAYER);
        add(modal, MODAL_LAYER);
        ir(IdPantalla.MENU);
    }

    @Override
    public void doLayout() {
        contenedor.setBounds(0, 0, getWidth(), getHeight());
        modal.setBounds(0, 0, getWidth(), getHeight());
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1280, 720);
    }

    @Override
    public void ir(IdPantalla destino) {
        modal.cerrar();
        actual = pantallas.get(destino);
        cartas.show(contenedor, destino.name());
        actual.alMostrar();
    }

    @Override
    public void editarPerfil(int numero, Runnable alGuardar) {
        ((PantallaPersonalizar) pantallas.get(IdPantalla.PERFIL)).configurar(numero, alGuardar);
        ir(IdPantalla.PERFIL);
    }

    @Override
    public void mostrarModal(JComponent contenido, boolean cerrarAlClicFuera, Component huecoDe,
                             Color velo, Runnable alCerrar) {
        modal.mostrar(contenido, cerrarAlClicFuera, huecoDe, velo, alCerrar);
    }

    @Override
    public void cerrarModal() {
        modal.cerrar();
    }

    @Override
    public boolean hayModal() {
        return modal.estaAbierta();
    }

    @Override
    public void recargar() {
        if (actual != null) {
            actual.alMostrar();
        }
    }

    @Override
    public void salir() {
        System.exit(0);
    }
}
