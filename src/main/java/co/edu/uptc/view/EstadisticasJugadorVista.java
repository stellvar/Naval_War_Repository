package co.edu.uptc.view;

/* estadisticas de un jugador para el panel emergente */
public record EstadisticasJugadorVista(String nombre, String biografia, int avatar,
                                       int jugadas, int ganadas, int perdidas, int mejorTurnos) {
}
