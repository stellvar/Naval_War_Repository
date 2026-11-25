package co.edu.uptc.view;

/* resultado de un disparo; tamanoBarco solo aplica cuando el tipo es HUNDIDO */
public record ResultadoDisparo(Tipo tipo, int tamanoBarco) {

    public enum Tipo { INVALIDO, AGUA, TOCADO, HUNDIDO }

    public static ResultadoDisparo de(Tipo tipo) {
        return new ResultadoDisparo(tipo, 0);
    }
}
