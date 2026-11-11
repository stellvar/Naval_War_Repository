package co.edu.uptc.view;

import javax.swing.JComponent;
import java.awt.Color;
import java.awt.Component;


public interface Navegador {

    void ir(IdPantalla destino);

    /*
     * @param numero    1 o 2 (jugador que se está personalizando)
     * @param alGuardar se ejecuta al pulsar GUARDAR (puede ser null)
     */
    void editarPerfil(int numero, Runnable alGuardar);

    void mostrarModal(JComponent contenido, boolean cerrarAlClicFuera, Component huecoDe,
                      Color velo, Runnable alCerrar);

    void cerrarModal();

    boolean hayModal();



    void recargar();

    void salir();
}
