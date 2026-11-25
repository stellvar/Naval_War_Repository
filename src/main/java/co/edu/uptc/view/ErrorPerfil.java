package co.edu.uptc.view;


public enum ErrorPerfil {
    NOMBRE_NO_VALIDO("perfil.errorNombre"),
    NOMBRES_IGUALES("perfil.errorIguales");

    private final String clave;

    ErrorPerfil(String clave) {
        this.clave = clave;
    }

    public String getClave() {
        return clave;
    }
}
